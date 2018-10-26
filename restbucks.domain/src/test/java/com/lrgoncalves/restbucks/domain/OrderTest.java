package com.lrgoncalves.restbucks.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.lrgoncalves.neo4j.jndi.binding.Neo4jBasicAuthentication;
import com.lrgoncalves.restbucks.repositories.neo4j.OrderNeo4jRepository;


public class OrderTest {

	public static void main(String[] args) {

		try{

/*			String endpoint = "http://localhost:7474/db/data/";

			OrderNeo4jRepository orderNeo4jRepository = new OrderNeo4jRepository();
			
			Client client = new Client();
			
			HTTPBasicAuthFilter feature = new HTTPBasicAuthFilter("neo4j",  "123456".getBytes());
			
			client = Client.create();
			
			client.getProperties().put("host", endpoint );
			
			client.addFilter(feature);
			

			orderNeo4jRepository.setRepository(client);*/
			
			
			
			
			//cff7dc61-3a21-4c5c-973b-3bc6b0ee1079
			//Order order = orderNeo4jRepository.get(new Identifier("7b1c92ad-7833-43eb-89f7-74721bc85c34"));
			
			Neo4jBasicAuthentication basicAuthentication = new Neo4jBasicAuthentication();
			
			Hashtable<String, String> environment = new Hashtable<String, String>();

			environment.put("host", "http://localhost:7474/db/data/");
			environment.put("username", "neo4j");
			environment.put("password", "123456");

			Properties urlConnection = (Properties) basicAuthentication.getObjectInstance(null, null, null, environment);

			//Client	jerseyClient = Client.create();
			
			//Neo4JClient that = Neo4JClient.setClient(urlConnection).getInstance();
			
			
			Order order = new Order();
			order.setLocation(Location.IN_STORE);
			
			order.addItem(new Item(Size.LARGE, Milk.NONE, Drink.CAPPUCCINO));
			
			OrderNeo4jRepository orderNeo4jRepository = new OrderNeo4jRepository();
			
			orderNeo4jRepository.setRepository(urlConnection);
			
			orderNeo4jRepository.store(order);
			
			System.out.println(order.getIdentifier());

		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	@Test
	public void shouldStoreAndRetrieveItems() {
		int numberOfItems = 5;
		ArrayList<Item> items = new ArrayList<Item>();

		for(int i = 0; i < numberOfItems; i++) {
			items.add(new Item(Size.SMALL, Milk.WHOLE, Drink.LATTE));
		}

		Order order = new Order(Location.TAKEAWAY, OrderStatus.UNPAID, items);
		assertEquals(numberOfItems, order.getItems().size());
	}

	@Test
	public void shouldCalculateCost() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(Size.SMALL, Milk.NONE, Drink.ESPRESSO));
		items.add(new Item(Size.LARGE, Milk.WHOLE, Drink.LATTE));
		items.add(new Item(Size.LARGE, Milk.SEMI, Drink.CAPPUCCINO));

		Order order = new Order(Location.TAKEAWAY, items);
		order.calculateCost();
		assertEquals(5.5, order.calculateCost(), 0.0);
	}
}
