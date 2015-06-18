package com.lrgoncalves.restbucks.client.activities;

import java.net.URI;

import com.lrgoncalves.restbucks.client.ClientOrder;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;

public class UpdateOrderActivity extends Activity {

    private final URI updateUri;
    private OrderRepresentation updatedOrderRepresentation;

    public UpdateOrderActivity(URI updateUri) {
        this.updateUri = updateUri;
    }

    public void updateOrder(Order order) {
        try {
            updatedOrderRepresentation = binding.updateOrder(order, updateUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromOrderRepresentation(updatedOrderRepresentation);
        } catch (MalformedOrderException e) {
            actions = retryCurrentActivity();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (CannotUpdateOrderException e) {
            actions = noFurtherActivities();
        }
    }
    
    public ClientOrder getOrder() {
        return new ClientOrder(updatedOrderRepresentation.getOrder());
    }
}
