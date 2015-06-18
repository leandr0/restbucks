package com.lrgoncalves.restbucks.activities;

import com.lrgoncalves.restbucks.representations.RestbucksUri;

public class UriExchange {

    public static RestbucksUri paymentForOrder(RestbucksUri orderUri) {
        checkForValidOrderUri(orderUri);
        return new RestbucksUri(orderUri.getBaseUri() + "/payment/" + orderUri.getId().toString());
    }
    
    public static RestbucksUri orderForPayment(RestbucksUri paymentUri) {
        checkForValidPaymentUri(paymentUri);
        return new RestbucksUri(paymentUri.getBaseUri() + "/order/" + paymentUri.getId().toString());
    }

    public static RestbucksUri receiptForPayment(RestbucksUri paymentUri) {
        checkForValidPaymentUri(paymentUri);
        String url = paymentUri.toString().replaceAll("/payment/.*", "");
        return new RestbucksUri(url + "/receipt/" + paymentUri.getId().toString());
    }
    
    public static RestbucksUri orderForReceipt(RestbucksUri receiptUri) {
        checkForValidReceiptUri(receiptUri);
        String url = receiptUri.toString().replaceAll("/receipt.*", "");
        return new RestbucksUri(url + "/order/" + receiptUri.getId().toString());
    }
    
    private static void checkForValidOrderUri(RestbucksUri orderUri) {
        if(!orderUri.toString().contains("/order/")) {
            throw new RuntimeException("Invalid Order URI");
        }
    }
    
    private static void checkForValidPaymentUri(RestbucksUri payment) {
        if(!payment.toString().contains("/payment/")) {
            throw new RuntimeException("Invalid Payment URI");
        }
    }
    
    private static void checkForValidReceiptUri(RestbucksUri receipt) {
        if(!receipt.toString().contains("/receipt/")) {
            throw new RuntimeException("Invalid Receipt URI");
        }
    }
}
