package com.lrgoncalves.restbucks.client.activities;

import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.PaymentRepresentation;

class RepresentationHypermediaProcessor {

    Actions extractNextActionsFromOrderRepresentation(OrderRepresentation representation) {
        Actions actions = new Actions();

        if (representation != null) {

            if (representation.getHypermedia().getPaymentLink() != null) {
                actions.add(new PaymentActivity(representation.getHypermedia().getPaymentLink().get(0).getUri()));
            }

            if (representation.getHypermedia().getUpdateLink() != null) {
                actions.add(new UpdateOrderActivity(representation.getHypermedia().getUpdateLink().get(0).getUri()));
            }

            if (representation.getHypermedia().getSelfLink() != null) {
                actions.add(new ReadOrderActivity(representation.getHypermedia().getSelfLink().get(0).getUri()));
            }

            if (representation.getHypermedia().getCancelLink() != null) {
                actions.add(new CancelOrderActivity(representation.getHypermedia().getCancelLink().get(0).getUri()));
            }
        }

        return actions;
    }

    public Actions extractNextActionsFromPaymentRepresentation(PaymentRepresentation representation) {
        Actions actions = new Actions();
        
        if(representation.getHypermedia().getOrderLink() != null) {
            actions.add(new ReadOrderActivity(representation.getHypermedia().getOrderLink().get(0).getUri()));
        }
        
        if(representation.getHypermedia().getReceiptLink() != null) {
            actions.add(new GetReceiptActivity(representation.getHypermedia().getReceiptLink().get(0).getUri()));
        }
        
        return actions;
    }

}
