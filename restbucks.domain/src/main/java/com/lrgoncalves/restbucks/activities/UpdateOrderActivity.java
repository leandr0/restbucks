package com.lrgoncalves.restbucks.activities;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.repositories.neo4j.OrderNeo4jRepository;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

@Named
public class UpdateOrderActivity {
	
	@Inject
	private OrderNeo4jRepository repository;
	
	
    public OrderRepresentation update(Order order, RestbucksUri orderUri) throws IOException {
        
    	Identifier orderIdentifier = orderUri.getId();
        
        if (repository.orderNotPlaced(orderIdentifier)) { // Defensive check to see if we have the order
            throw new NoSuchOrderException();
        }

        if (!orderCanBeChanged(orderIdentifier)) {
            throw new UpdateException();
        }

        repository.store(orderIdentifier, order);
        
        Order storedOrder = repository.get(orderIdentifier);
        
        storedOrder.setStatus(storedOrder.getStatus());
        storedOrder.calculateCost();        

        return OrderRepresentation.createResponseOrderRepresentation(storedOrder, orderUri); 
    }
    
    private boolean orderCanBeChanged(Identifier identifier) throws IOException {
        return repository.get(identifier).getStatus() == OrderStatus.UNPAID;
    }
}
