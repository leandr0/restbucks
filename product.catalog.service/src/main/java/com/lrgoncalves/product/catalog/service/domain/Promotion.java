package com.lrgoncalves.product.catalog.service.domain;

import java.io.StringWriter;
import java.net.URI;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@SuppressWarnings("unused")
@XmlRootElement(name = "promotion", namespace = Promotion.PROMOTION_NAMESPACE)
public class Promotion extends Event {
	public static final String PROMOTION_NAMESPACE = Event.RESTBUCKS_NAMESPACE + "/promotion";

    private static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
	
	@XmlElement(namespace = PROMOTION_NAMESPACE)
	private String effective;
	@XmlElement(name = "product", namespace = ATOM_NAMESPACE)
	private List<Link> appliesToProducts;
	@XmlElement(name = "region", namespace = ATOM_NAMESPACE)
	private List<Link> appliesToRegions;
	
	private Promotion() {} // For JAXB :-(
	
	public Promotion(Date effectiveFrom, List<Link> products, List<Link> regions, URI uri, boolean isUpdate, DateTime createdAt, String createdBy) {
		super(uri, isUpdate, createdAt, createdBy);
	    effective = effectiveFrom.toString();
		appliesToProducts = products;
		appliesToRegions = regions;
		updated = createdAt;
	}
	
    public String getEventType() {
        return "promotion";
    }
    
    public String toXmlString() {
        try {
            JAXBContext context = JAXBContext.newInstance(Promotion.class);
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
