package com.lrgoncalves.restbucks.representations;

import static com.lrgoncalves.restbucks.representations.Representation.jsonMapper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lrgoncalves.restbucks.domain.Hypermedia;
import com.lrgoncalves.restbucks.domain.Payment;


@XmlRootElement(name = "payment", namespace = Representation.RESTBUCKS_NAMESPACE)
public class PaymentRepresentation extends Representation {
       
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private double 	amount;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private String 	cardholderName;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private String 	cardNumber;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private int 		expiryMonth;
    @XmlElement(namespace = PaymentRepresentation.RESTBUCKS_NAMESPACE) private int 		expiryYear;
    
    
    /**
     * For JAXB :-(
     */
     public PaymentRepresentation(){}
    
    public PaymentRepresentation(Payment payment, Hypermedia hypermedia) {
        amount 			= payment.getAmount();
        cardholderName 	= payment.getCardholderName();
        cardNumber 		= payment.getCardNumber();
        expiryMonth 	= payment.getExpiryMonth();
        expiryYear 		= payment.getExpiryYear();
        this.hypermedia = hypermedia;
    }

    //        	jsonMapper.readValue(paymentRepresentation, OrderRepresentation.class);
    public Payment getPayment() {
        return new Payment(amount, cardholderName, cardNumber, expiryMonth, expiryYear);
    }

	public double getAmount() {
		return amount;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public int getExpiryMonth() {
		return expiryMonth;
	}

	public int getExpiryYear() {
		return expiryYear;
	}
}