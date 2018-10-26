package com.lrgoncalves.restbucks.activities;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.repositories.neo4j.OrderNeo4jRepository;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

@Named
public class ReadOrderActivity {
	
	@Inject
	private OrderNeo4jRepository repository;
	
    public OrderRepresentation retrieveByUri(RestbucksUri orderUri) throws IOException {
        Identifier identifier  = orderUri.getId();
        
        Order order = repository.get(identifier);
        
        if(order == null) {
            throw new NoSuchOrderException();
        }
        
        return OrderRepresentation.createResponseOrderRepresentation(order, orderUri);
    }
}
