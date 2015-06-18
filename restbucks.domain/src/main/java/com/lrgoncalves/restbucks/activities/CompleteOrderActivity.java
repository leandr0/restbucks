package com.lrgoncalves.restbucks.activities;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.repositories.OrderRepository;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;

public class CompleteOrderActivity {

    public OrderRepresentation completeOrder(Identifier identifier) {
    	
        OrderRepository repository = OrderRepository.current();
        
        if (repository.has(identifier)) {
            Order order = repository.get(identifier);

            if (order.getStatus() == OrderStatus.READY) {
                order.setStatus(OrderStatus.TAKEN);
            }else if (order.getStatus() == OrderStatus.TAKEN) {
                throw new OrderAlreadyCompletedException();
            }else if (order.getStatus() == OrderStatus.UNPAID) {
                throw new OrderNotPaidException();
            }

            Order ordering = OrderRepository.current().get(identifier);
            
            ordering.setStatus(OrderStatus.TAKEN);
            
            OrderRepository.current().store(identifier, ordering);
            
            return new OrderRepresentation(order);
            
        } else {
            throw new NoSuchOrderException();
        }
    }
}
