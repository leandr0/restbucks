package com.lrgoncalves.restbucks.repositories.neo4j;

import static com.lrgoncalves.neo4j.QueryCypherParamType.IDENTIFIER;
import static com.lrgoncalves.restbucks.domain.query.OrderCypherQueryType.FIND_BY_IDENTIFIER;
import static com.lrgoncalves.restbucks.domain.query.OrderCypherQueryType.FIND_FIRST_PATH_BY_IDENTIFIER;

import java.io.IOException;
import java.util.*;

import javax.inject.Inject;

import org.neo4j.graphdb.Label;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.neo4j.rest.graphdb.util.QueryResult;

import com.lrgoncalves.neo4j.Neo4JClient;
import com.lrgoncalves.neo4j.Node;
import com.lrgoncalves.restbucks.domain.Drink;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Item;
import com.lrgoncalves.restbucks.domain.Location;
import com.lrgoncalves.restbucks.domain.Milk;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.domain.Size;
import com.lrgoncalves.restbucks.domain.query.OrderCypherQuery;
import com.lrgoncalves.restbucks.repositories.GraphRepository;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;

public class OrderNeo4jRepository implements GraphRepository<Order>{
	
	@Inject
	private Neo4JClient neo4jClient;


	@Override
	public Order get(Identifier identifier) throws IOException {

		Order order = new Order();

		OrderCypherQuery orderCypherQuery = new OrderCypherQuery();

		orderCypherQuery.setParameter(IDENTIFIER, identifier.toString());

		//QueryResult<Map<String, Object>> result = null;
		
		Collection<Map<String,Map<String,String>>> jsonRepresentation = null;
		
		try {
			//result 
			jsonRepresentation = neo4jClient
					.sendTransactionalCypherQuery(FIND_FIRST_PATH_BY_IDENTIFIER,orderCypherQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        for (Map<String,Map<String,String>> rowMap : jsonRepresentation ) {

            Map.Entry<String,Map<String,String>> entry= rowMap.entrySet().iterator().next();

            final String NODE_LABEL = entry.getKey();
            Map<String,String> restNode = entry.getValue();
                    
            if(NODE_LABEL.equals(Order.LABEL)){

                order.setIdentifier(new Identifier(restNode.get("identifier").toString()));

            }else if(NODE_LABEL.equals(Item.LABEL)){

                Drink drink = Drink.valueOf(""+restNode.get("drink"));
                Size  size  = Size.valueOf(""+restNode.get("size"));
                Milk  milk  = Milk.valueOf(""+restNode.get("milk"));

                order.addItem( new Item(size,milk,drink));

            }else if(NODE_LABEL.equals(Location.LABEL)){

                order.setLocation(Location.valueOf(""+restNode.get("value")));
            }

        }


		/**
		List<Map<String, Object>> nodes = IteratorUtil.asList(result.iterator());

		for (Map<String, Object> mapNode : nodes) {

			RestNode restNode = (RestNode) mapNode.get("nodes");

			final Label NODE_LABEL = IteratorUtil.asList(restNode.getLabels()).get(0);

			if(NODE_LABEL.equals(Order.LABEL)){

				order.setIdentifier(new Identifier(restNode.getProperty("identifier").toString()));

			}else if(NODE_LABEL.equals(Item.LABEL)){

				Drink drink = Drink.valueOf(""+restNode.getProperty("drink"));
				Size  size  = Size.valueOf(""+restNode.getProperty("size"));
				Milk  milk  = Milk.valueOf(""+restNode.getProperty("milk"));

				order.addItem( new Item(size,milk,drink));	

			}else if(NODE_LABEL.equals(Location.LABEL)){

				order.setLocation(Location.valueOf(""+restNode.getProperty("value")));
			}
		}
        **/
		return order;
	}

	@Override
	public Order take(Identifier identifier) throws IOException {

		OrderCypherQuery orderCypherQuery = new OrderCypherQuery();

		orderCypherQuery.setParameter(IDENTIFIER, identifier.toString());

		QueryResult<Map<String, Object>> result = null;
		try {
			/*result = neo4jClient
					.sendTransactionalCypherQuery(FIND_BY_IDENTIFIER,orderCypherQuery);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Iterator<Map<String, Object>> iterator = result.iterator(); 

		Map<String,Object> 	row 	= null;
		Iterable<String> 	keys 	= null;

		while(iterator.hasNext()) {  

			row		= iterator.next();			
			keys 	= ((RestNode)row.get("item")).getPropertyKeys();

			for (String string : keys) {
				System.out.println(string+" : "+((RestNode)row.get("item")).getProperty(string));	
			}
		}
		return null;
	}

	@Override
	public Identifier store(Order entity) throws RepositoryStoreException{

		if(Objects.isNull(entity) || !entity.getStatus().equals(OrderStatus.UNPAID)){
			throw new IllegalArgumentException("Object or Order Status invalid");
		}
		
		try{

			entity.setIdentifier(new Identifier());

			Node orderNode = neo4jClient.createNode(Order.LABEL)
					.addProperty("cost", ""+entity.calculateCost())
					.addProperty("identifier", ""+entity.getIdentifier());

			for(Item item : entity.getItems()){

				Node itemNode = neo4jClient.createNode(Item.LABEL)
						.addProperty("size", item.getSize().name())
						.addProperty("milk", item.getMilk().name())
						.addProperty("drink",item.getDrink().name());

				orderNode.addAuthenticationRequest(neo4jClient.authorizationKey())
							.addRelationship(itemNode, "has", null);
			}

			Node locationNode = neo4jClient.createNode(Location.LABEL)
					.addProperty("value", Location.TAKEAWAY.name());

			orderNode.addAuthenticationRequest(neo4jClient.authorizationKey())
					.addRelationship(locationNode, "belongs", null);

			Node statusNode = neo4jClient.createNode(OrderStatus.LABEL)
					.addProperty("value", entity.getStatus().name());

			orderNode.addAuthenticationRequest(neo4jClient.authorizationKey())
						.addRelationship(statusNode, "status", null);

			return entity.getIdentifier();

		}catch(Exception e){
			//TODO: Add Logger
			throw new RepositoryStoreException(e);
		}
	}

	@Override
	public void store(Identifier identifier, Order entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Identifier identifier) {
		// TODO Auto-generated method stub

	}

	public boolean orderPlaced(Identifier identifier) throws IOException {

		OrderCypherQuery orderCypherQuery = new OrderCypherQuery();

		orderCypherQuery.setParameter(IDENTIFIER, identifier.toString());

		QueryResult<Map<String, Object>> result = null;
		
		try {

			/*result =*/
                    neo4jClient
					.sendTransactionalCypherQuery(FIND_BY_IDENTIFIER,orderCypherQuery);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Map<String, Object>> nodes =   IteratorUtil.asList(result.iterator());

		return !nodes.isEmpty();
	}

	public boolean orderNotPlaced(Identifier identifier) throws IOException {
		return !orderPlaced(identifier);
	}

	/**
	 * Used for Tests
	 * @param client
	 * @throws IOException 
	 */
	public void setRepository(final Properties client) throws IOException{

		//this.repository = new Neo4jRepository();

		//Neo4JClient neo4jClient = Neo4JClient.setClient(client);

		//this.repository.setNeo4jClient(neo4jClient);
	}
}
