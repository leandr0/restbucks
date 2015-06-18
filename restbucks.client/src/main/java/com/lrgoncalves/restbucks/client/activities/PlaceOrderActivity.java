package com.lrgoncalves.restbucks.client.activities;

import java.net.URI;

import com.lrgoncalves.restbucks.client.ClientOrder;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;

public class PlaceOrderActivity extends Activity {

    private Order order;

    public void placeOrder(Order order, URI orderingUri) {
        
        try {
            OrderRepresentation createdOrderRepresentation = binding.createOrder(order, orderingUri);
            this.actions = new RepresentationHypermediaProcessor().extractNextActionsFromOrderRepresentation(createdOrderRepresentation);
            this.order = createdOrderRepresentation.getOrder();
        } catch (MalformedOrderException e) {
            this.actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            this.actions = retryCurrentActivity();
        }
    }
    
    public ClientOrder getOrder() {
        return new ClientOrder(order);
    }
}
