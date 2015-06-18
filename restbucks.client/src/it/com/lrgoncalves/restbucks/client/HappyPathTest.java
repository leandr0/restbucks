package com.lrgoncalves.restbucks.client;

import static com.lrgoncalves.restbucks.domain.OrderBuilder.order;
import static com.lrgoncalves.restbucks.domain.PaymentBuilder.payment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lrgoncalves.restbucks.client.activities.Actions;
import com.lrgoncalves.restbucks.client.activities.GetReceiptActivity;
import com.lrgoncalves.restbucks.client.activities.PaymentActivity;
import com.lrgoncalves.restbucks.client.activities.PlaceOrderActivity;
import com.lrgoncalves.restbucks.client.activities.ReadOrderActivity;
import com.lrgoncalves.restbucks.client.activities.UpdateOrderActivity;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.domain.Payment;
import com.lrgoncalves.restbucks.representations.Link;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.PaymentRepresentation;
import com.lrgoncalves.restbucks.representations.ReceiptRepresentation;
import com.sun.jersey.api.client.Client;

public class HappyPathTest {
    
    private static final String RESTBUCKS_MEDIA_TYPE = "application/vnd.restbucks+xml";

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
    public void shouldBeAbleToDriveTheProtocolThroughTheHappyStates() throws Exception {
        Order order = order().withRandomItems().build();
        
        PlaceOrderActivity placeOrderActivity = new PlaceOrderActivity();
        placeOrderActivity.placeOrder(order, new URI(TestHelper.BASE_URI + "order"));
        Actions actions =  placeOrderActivity.getActions();
        
        if(actions.has(UpdateOrderActivity.class)) {
            UpdateOrderActivity updateOrderActivity = actions.get(UpdateOrderActivity.class);
            updateOrderActivity.updateOrder(order);
            actions = updateOrderActivity.getActions();
        }
        
        ReadOrderActivity readOrderActivity = null;
        if(actions.has(ReadOrderActivity.class)) {
            readOrderActivity = actions.get(ReadOrderActivity.class);
            readOrderActivity.readOrder();
            actions = readOrderActivity.getActions();
            // Can we read the order resource?
            assertNotNull(readOrderActivity.getOrder().toString());
        }
        
        if(actions.has(PaymentActivity.class)) {
            PaymentActivity paymentActivity = actions.get(PaymentActivity.class);
            paymentActivity.payForOrder(payment().withAmount(readOrderActivity.getOrder().getCost()).build());
            actions = paymentActivity.getActions();
        }
        
        if(actions.has(GetReceiptActivity.class)) {
            GetReceiptActivity getReceiptActivity = actions.get(GetReceiptActivity.class);
            getReceiptActivity.getReceiptForOrder();
            actions = getReceiptActivity.getActions();
            assertNotNull(getReceiptActivity.getReceipt());
        }
    }
    
    @Test
    public void shouldBeAbleToDriveTheProtocolThroughTheHappyStatesUsingLinkDataOnly() throws Exception {
        Order order = order().withRandomItems().build();
        Client client = Client.create();
        OrderRepresentation orderRepresentation = client.resource(new URI(TestHelper.BASE_URI + "order")).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new OrderRepresentation(order));
        
        // Change the order
        order = order().withRandomItems().build();
        Link updateLink = orderRepresentation.getHypermedia().getUpdateLink().get(0);
        OrderRepresentation updatedRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(OrderRepresentation.class, new OrderRepresentation(order));
        
        // Pay for the order 
        Link paymentLink = updatedRepresentation.getHypermedia().getPaymentLink().get(0);
        Payment payment = new Payment(updatedRepresentation.getCost(), "A.N. Other", "12345677878", 12, 2999);
        PaymentRepresentation  paymentRepresentation = client.resource(paymentLink.getUri()).accept(paymentLink.getMediaType()).type(paymentLink.getMediaType()).put(PaymentRepresentation.class, new PaymentRepresentation(payment,null));
        
        // Get a receipt
        Link receiptLink = paymentRepresentation.getHypermedia().getReceiptLink().get(0);
        ReceiptRepresentation receiptRepresentation = client.resource(receiptLink.getUri()).get(ReceiptRepresentation.class);
        
        // Finally, check on the order status
        
        
        Link orderLink = receiptRepresentation.getHypermedia().getOrderLink().get(0);
        OrderRepresentation finalOrderRepresentation = client.resource(orderLink.getUri()).accept(RESTBUCKS_MEDIA_TYPE).get(OrderRepresentation.class);
        assertEquals(OrderStatus.PREPARING, finalOrderRepresentation.getStatus());
    }
}
