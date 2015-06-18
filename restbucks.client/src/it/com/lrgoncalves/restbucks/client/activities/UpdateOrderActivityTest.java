package com.lrgoncalves.restbucks.client.activities;

import static com.lrgoncalves.restbucks.domain.OrderBuilder.order;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lrgoncalves.restbucks.client.TestHelper;
import com.lrgoncalves.restbucks.client.activities.Actions;
import com.lrgoncalves.restbucks.client.activities.UpdateOrderActivity;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.repositories.OrderRepository;

public class UpdateOrderActivityTest {
    
    @Before
    public void startServer() {
        try {
            TestHelper.getInstance().startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void stopServer() {
        TestHelper.getInstance().stopServer();
    }
    
    @Test
    public void shouldBeAbleToUpdateOrder() throws Exception {
        Identifier identifier = placeOrder();
        URI orderUri = new URI(TestHelper.BASE_URI + "order/" + identifier.toString());
        
        UpdateOrderActivity activity = new UpdateOrderActivity(orderUri);
        activity.updateOrder(order().withRandomItems().build());
        Actions actions = activity.getActions();
        
        int numberOfOPermissibleActionsFollowingSuccessfulCreation = 4;
        assertEquals(numberOfOPermissibleActionsFollowingSuccessfulCreation, actions.size());
    }
    
    @Test
    public void shouldFailToUpdateNonExistentOrder() throws Exception {
        URI orderUri = new URI(TestHelper.BASE_URI + "order/does-not-exist");
        UpdateOrderActivity activity = new UpdateOrderActivity(orderUri);
        activity.updateOrder(order().withRandomItems().build());
        Actions actions = activity.getActions();
        
        int numberOfActionsAvailableForNonExistentOrder = 0;
        assertEquals(numberOfActionsAvailableForNonExistentOrder, actions.size());
    }

    private Identifier placeOrder() {
        Order order = order().withRandomItems().build();
        Identifier identifier = OrderRepository.current().store(order);
        return identifier;
    }
}
