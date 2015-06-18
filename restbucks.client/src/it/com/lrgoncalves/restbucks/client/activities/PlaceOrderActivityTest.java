package com.lrgoncalves.restbucks.client.activities;

import static com.lrgoncalves.restbucks.domain.OrderBuilder.order;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lrgoncalves.restbucks.client.TestHelper;
import com.lrgoncalves.restbucks.client.activities.Actions;
import com.lrgoncalves.restbucks.client.activities.PlaceOrderActivity;
import com.lrgoncalves.restbucks.domain.Order;


public class PlaceOrderActivityTest {

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
    public void shouldPlaceOrder() throws Exception {
        PlaceOrderActivity activity = new PlaceOrderActivity();
        
        Order order = order().withRandomItems().build();
        
        activity.placeOrder(order, new URI(TestHelper.BASE_URI + "order"));
        Actions actions = activity.getActions();
        
        int numberOfOPermissibleActionsFollowingSuccessfulCreation = 4;
        assertEquals(numberOfOPermissibleActionsFollowingSuccessfulCreation, actions.size());
    }
}
