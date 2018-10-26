package com.lrgoncalves.restbucks.activities;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.repositories.neo4j.OrderNeo4jRepository;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

@Named
public class CreateOrderActivity {
 

	@Inject
	private OrderNeo4jRepository repository;
	
	private static final Log LOG = LogFactory.getLog(CreateOrderActivity.class);
	
	public OrderRepresentation create(Order order, RestbucksUri requestUri) throws CreateOrderException{
					
		order.setStatus(OrderStatus.UNPAID);
		
		Identifier identifier = null;
		
		try{
                
			identifier = repository.store(order);
			
		}
		catch(IllegalArgumentException e){
			LOG.warn(e.getMessage());
			//TODO Return HTTP Code Status 400
			return new OrderRepresentation(order);
		}
		catch(Exception e){
			
			LOG.error(e.getMessage());
			
			throw new CreateOrderException(e.getMessage());
			
		}
		
        return new OrderRepresentation(order, HypermediaActivity.create(identifier, requestUri));
    }
}