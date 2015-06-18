package com.lrgoncalves.restbucks.client.activities;

import java.net.URI;

import com.lrgoncalves.restbucks.client.ClientOrder;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;

public class ReadOrderActivity extends Activity {

    private final URI orderUri;
    private OrderRepresentation currentOrderRepresentation;

    public ReadOrderActivity(URI orderUri) {
        this.orderUri = orderUri;
    }

    public void readOrder() {
        try {
            currentOrderRepresentation = binding.retrieveOrder(orderUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromOrderRepresentation(currentOrderRepresentation);
        } catch (NotFoundException e) {
            actions = new Actions();
            actions.add(new PlaceOrderActivity());
        } catch (ServiceFailureException e) {
            actions = new Actions();
            actions.add(this);
        }
    }

    public ClientOrder getOrder() {
        return new ClientOrder(currentOrderRepresentation.getOrder());
    }
}
