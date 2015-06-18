package com.lrgoncalves.restbucks.client.activities;

import static com.lrgoncalves.restbucks.domain.OrderBuilder.order;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lrgoncalves.restbucks.client.TestHelper;
import com.lrgoncalves.restbucks.client.activities.Actions;
import com.lrgoncalves.restbucks.client.activities.CancelOrderActivity;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.repositories.OrderRepository;

public class CancelOrderActivityTest {
    
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
    public void shouldBeAbleToCancelOrder() throws Exception {
        
        Identifier identifier = placeOrder();
        URI orderUri = new URI(TestHelper.BASE_URI + "order/" + identifier.toString());
        
        CancelOrderActivity activity = new CancelOrderActivity(orderUri);
        
        activity.cancelOrder();
        Actions actions = activity.getActions();
        
        int numberOfValidActivitiesAfterCancelling = 0;
        assertEquals(numberOfValidActivitiesAfterCancelling, actions.size());
    }
    
    private Identifier placeOrder() {
        Order order = order().withRandomItems().build();
        Identifier identifier = OrderRepository.current().store(order);
        return identifier;
    }
}
