package com.lrgoncalves.restbucks.activities;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.repositories.OrderRepository;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

public class CreateOrderActivity {
 
	public OrderRepresentation create(Order order, RestbucksUri requestUri) {
        
		order.setStatus(OrderStatus.UNPAID);
                
        Identifier identifier = OrderRepository.current().store(order);
        
        return new OrderRepresentation(order, HypermediaActivity.create(identifier, requestUri));
    }
}