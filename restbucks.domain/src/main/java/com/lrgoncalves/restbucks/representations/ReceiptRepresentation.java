package com.lrgoncalves.restbucks.representations;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.lrgoncalves.restbucks.domain.Hypermedia;
import com.lrgoncalves.restbucks.domain.Payment;

@XmlRootElement(name = "receipt", namespace = Representation.RESTBUCKS_NAMESPACE)
public class ReceiptRepresentation extends Representation {

    @XmlElement(name = "amount", namespace = Representation.RESTBUCKS_NAMESPACE)
    private double amountPaid;
    @XmlElement(name = "paid", namespace = Representation.RESTBUCKS_NAMESPACE)
    private String paymentDate;
    
    ReceiptRepresentation(){} // For JAXB :-(
    
    public ReceiptRepresentation(Payment payment, Hypermedia hypermedia) {
        this.amountPaid = payment.getAmount();
        this.paymentDate = payment.getPaymentDate().toString();
        this.hypermedia = hypermedia;
    }

    public DateTime getPaidDate() {
        return new DateTime(paymentDate);
    }
    
    public double getAmountPaid() {
        return amountPaid;
    }
    
    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(ReceiptRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
