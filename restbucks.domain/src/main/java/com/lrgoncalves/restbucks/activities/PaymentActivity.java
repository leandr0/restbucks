package com.lrgoncalves.restbucks.activities;

import com.lrgoncalves.restbucks.domain.Hypermedia;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.domain.Payment;
import com.lrgoncalves.restbucks.repositories.OrderRepository;
import com.lrgoncalves.restbucks.repositories.PaymentRepository;
import com.lrgoncalves.restbucks.representations.PaymentRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

public class PaymentActivity {
    public PaymentRepresentation pay(Payment payment, RestbucksUri paymentUri) {
        Identifier identifier = paymentUri.getId();
        
        // Don't know the order!
        if(!OrderRepository.current().has(identifier)) {
            throw new NoSuchOrderException();
        }
        
        // Already paid
        if(PaymentRepository.current().has(identifier)) {
            throw new UpdateException();
        }
        
        // Business rules - if the payment amount doesn't match the amount outstanding, then reject
        if(OrderRepository.current().get(identifier).calculateCost() != payment.getAmount()) {
            throw new InvalidPaymentException();
        }
        
        // If we get here, let's create the payment and update the order status
        OrderRepository.current().get(identifier).setStatus(OrderStatus.PREPARING);
        PaymentRepository.current().store(identifier, payment);
        
       Hypermedia hypermedia = HypermediaActivity.createForPayment(paymentUri);
        		
        return new PaymentRepresentation(payment, hypermedia);
    }
}
