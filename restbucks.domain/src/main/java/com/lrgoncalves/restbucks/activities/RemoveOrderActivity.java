package com.lrgoncalves.restbucks.activities;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.repositories.OrderRepository;
import com.lrgoncalves.restbucks.repositories.neo4j.OrderNeo4jRepository;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
public class RemoveOrderActivity {

    @Inject
    private OrderNeo4jRepository repository;

    public OrderRepresentation delete(RestbucksUri orderUri) throws IOException {
        // Discover the URI of the order that has been cancelled
        
        Identifier identifier = orderUri.getId();

        Order order = repository.get(identifier);

        // Can't delete a ready or preparing order
        if (order.getStatus() == OrderStatus.PREPARING || order.getStatus() == OrderStatus.READY) {
            throw new OrderDeletionException();
        }

        if(order.getStatus() == OrderStatus.UNPAID) { // An unpaid order is being cancelled 
            repository.remove(identifier);
        }

        return new OrderRepresentation(order);
    }

}
