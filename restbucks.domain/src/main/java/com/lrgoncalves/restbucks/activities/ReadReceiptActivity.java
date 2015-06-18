package com.lrgoncalves.restbucks.activities;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.domain.Payment;
import com.lrgoncalves.restbucks.repositories.OrderRepository;
import com.lrgoncalves.restbucks.repositories.PaymentRepository;
import com.lrgoncalves.restbucks.representations.ReceiptRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

public class ReadReceiptActivity {

    public ReceiptRepresentation read(RestbucksUri receiptUri) {
    	
        Identifier identifier = receiptUri.getId();
        
        if(!orderHasBeenPaid(identifier)) {
            throw new OrderNotPaidException();
        } else if (OrderRepository.current().has(identifier) && OrderRepository.current().get(identifier).getStatus() == OrderStatus.TAKEN) {
            throw new OrderAlreadyCompletedException();
        }
        
        Payment payment = PaymentRepository.current().get(identifier);
        
        //return new ReceiptRepresentation(payment, HypermediaActivity.createRecieptLink(receiptUri));
        
        return new ReceiptRepresentation(payment, HypermediaActivity.createRecieptLink(receiptUri));
    }

    private boolean orderHasBeenPaid(Identifier id) {
        return PaymentRepository.current().has(id);
    }

}
