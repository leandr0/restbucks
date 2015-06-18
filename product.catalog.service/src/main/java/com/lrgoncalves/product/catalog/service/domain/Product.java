package com.lrgoncalves.product.catalog.service.domain;

import java.io.StringWriter;
import java.net.URI;
import java.text.NumberFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@SuppressWarnings("unused")
@XmlRootElement(name = "product", namespace = Product.PRODUCT_NAMESPACE)
public class Product extends Event {
	public static final String PRODUCT_NAMESPACE = Event.RESTBUCKS_NAMESPACE + "/product";
	
	@XmlElement(namespace = PRODUCT_NAMESPACE)
	private String name;
	@XmlElement(namespace = PRODUCT_NAMESPACE)
	private String size;
	@XmlElement(namespace = PRODUCT_NAMESPACE)
	private double price;
	
	private Product(){} // For JAXB :-(
	
	
	public Product(String name, String size, double price, URI productUri, boolean isUpdate, DateTime createdOn, String createdBy) {
		super(productUri, isUpdate, createdOn, createdBy);
	    this.name = name;
		this.size = size;
		this.price = roundToTwoDecimalPlaces(price);
	}
	
	private double roundToTwoDecimalPlaces(double price) {
	    NumberFormat nf = NumberFormat.getInstance();
	    nf.setMinimumFractionDigits(2);
	    nf.setMaximumFractionDigits(2);
	    return Double.valueOf(nf.format(price));
    }

    public Product(String name, double price, URI productUri, boolean isUpdate, DateTime createdOn, String createdBy) {
	    this(name, null, price, productUri, isUpdate, createdOn, createdBy);
	}

    public String getEventType() {
        return "product";
    }

    public String toXmlString() {
        try {
            JAXBContext context = JAXBContext.newInstance(Product.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



