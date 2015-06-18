package com.lrgoncalves.restbucks.client.activities;

import static com.lrgoncalves.restbucks.domain.OrderBuilder.order;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lrgoncalves.restbucks.client.TestHelper;
import com.lrgoncalves.restbucks.client.activities.Actions;
import com.lrgoncalves.restbucks.client.activities.GetReceiptActivity;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.domain.Payment;
import com.lrgoncalves.restbucks.repositories.OrderRepository;
import com.lrgoncalves.restbucks.repositories.PaymentRepository;


public class GetReceiptActivityTest {
    
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
    public void shouldBeAbleToRetreiveARecieptForPaidOrder() throws Exception {
        Identifier orderId = placeOrder();
        payForOrder(orderId);
        
        URI receiptUri = new URI(TestHelper.BASE_URI + "receipt/" + orderId.toString());
        
        GetReceiptActivity activity = new GetReceiptActivity(receiptUri);
        activity.getReceiptForOrder();
        Actions actions = activity.getActions();
        
        int numberOfActionsPermittedAfterGettingAReceipt = 1;
        assertEquals(numberOfActionsPermittedAfterGettingAReceipt , actions.size());
        
    }
    
    private void payForOrder(Identifier orderId) {
        Order order = OrderRepository.current().get(orderId);
        order.setStatus(OrderStatus.PREPARING);
        
        Payment payment = new Payment(order.calculateCost(), "Joe Strummer", "1952082120021222", 12, nextYear());
        PaymentRepository.current().store(orderId, payment);
    }

    private Identifier placeOrder() {
        Order order = order().withRandomItems().build();
        Identifier identifier = OrderRepository.current().store(order);
        return identifier;
    }
    
    private int nextYear() {
        return Calendar.getInstance().get(Calendar.YEAR) + 1;
    }
}



