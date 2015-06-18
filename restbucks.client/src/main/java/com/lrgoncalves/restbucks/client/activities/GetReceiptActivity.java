package com.lrgoncalves.restbucks.client.activities;

import java.net.URI;

import com.lrgoncalves.restbucks.representations.ReceiptRepresentation;

public class GetReceiptActivity extends Activity {
    private final URI receiptUri;
    private ReceiptRepresentation representation;

    public GetReceiptActivity(URI receiptUri) {
        this.receiptUri = receiptUri;
    }

    public void getReceiptForOrder() {
        try {
            representation = binding.retrieveReceipt(receiptUri);
            actions = new Actions();
            if(representation.getHypermedia().getOrderLink() != null) {
                actions.add(new ReadOrderActivity(representation.getHypermedia().getOrderLink().get(0).getUri()));
            } else {
                actions =  noFurtherActivities();
            }
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        }
    }

    public ReceiptRepresentation getReceipt() {
        return representation;
    }
}
