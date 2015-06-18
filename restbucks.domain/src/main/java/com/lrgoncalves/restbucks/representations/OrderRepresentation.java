package com.lrgoncalves.restbucks.representations;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.lrgoncalves.restbucks.activities.HypermediaActivity;
import com.lrgoncalves.restbucks.activities.InvalidOrderException;
import com.lrgoncalves.restbucks.domain.Hypermedia;
import com.lrgoncalves.restbucks.domain.Item;
import com.lrgoncalves.restbucks.domain.Location;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;

@XmlRootElement(name = "order", namespace = Representation.RESTBUCKS_NAMESPACE)
@JsonSerialize(include =Inclusion.NON_EMPTY)
public class OrderRepresentation extends Representation {

	@XmlElement(name = "location", namespace = Representation.RESTBUCKS_NAMESPACE)
	private Location location;

	@XmlElement(name = "cost", namespace = Representation.RESTBUCKS_NAMESPACE)
	private double cost;

	@XmlElement(name = "status", namespace = Representation.RESTBUCKS_NAMESPACE)
	private OrderStatus status;

	private Order order;

	/**
	 * For JAXB :-(
	 */
	public OrderRepresentation() {}

	public static OrderRepresentation fromXmlString(String xmlRepresentation) {
		try {
			JAXBContext context = JAXBContext.newInstance(OrderRepresentation.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (OrderRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
		} catch (Exception e) {
			throw new InvalidOrderException(e);
		}
	}

	public static OrderRepresentation fromJsonString(String jsonRepresentation) {
		try {			
			OrderRepresentation orderRepresentation = jsonMapper.readValue(jsonRepresentation, OrderRepresentation.class);
			return orderRepresentation;
		} catch (Exception e) {
			throw new InvalidOrderException(e);
		}
	}

	public static OrderRepresentation createResponseOrderRepresentation(Order order, RestbucksUri orderUri) {

		Hypermedia hypermedia = HypermediaActivity.create(orderUri);
		
		if(order.getStatus() == OrderStatus.UNPAID) {
			return new OrderRepresentation(order,hypermedia);
		} else if(order.getStatus() == OrderStatus.PREPARING) {
			return new OrderRepresentation(order, orderUri,Hypermedia.SELF_LINK);
		} else if(order.getStatus() == OrderStatus.READY) {
			return new OrderRepresentation(order, hypermedia.createRecieptLink(orderUri));
		} else if(order.getStatus() == OrderStatus.TAKEN) {
			return new OrderRepresentation(order);            
		} else {
			throw new RuntimeException("Unknown Order Status");
		}
	}

	public OrderRepresentation(Order order, Hypermedia hypermedia) {
		try {
			this.location = order.getLocation();
			this.order = order;
			this.cost = order.calculateCost();
			this.status = order.getStatus();
			this.hypermedia = hypermedia;
		} catch (Exception ex) {
			throw new InvalidOrderException(ex);
		}
	}
	
	public OrderRepresentation(Order order) {
		this(order, null);
	}

	public OrderRepresentation(Order order,RestbucksUri requestUri,String linkType) {
		try {
			this.location = order.getLocation();
			this.order = order;
			this.cost = order.calculateCost();
			this.status = order.getStatus();
			this.hypermedia = HypermediaActivity.create(requestUri);
			this.hypermedia.create(linkType);

		} catch (Exception ex) {
			throw new InvalidOrderException(ex);
		}
	}

	public Order getOrder() {
		if (this.location == null || this.order == null ||this.order.getItems() == null) {
			throw new InvalidOrderException();
		}
		for (Item i : order.getItems()) {
			if (i == null) {
				throw new InvalidOrderException();
			}
		}

		return new Order(location, status, order.getItems());
	}

	public OrderStatus getStatus() {
		return status;
	}

	public double getCost() {
		return cost;
	}

	public Location getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		try{
			return jsonMapper.writeValueAsString(this);
		}catch(Exception ex){
			return null;
		}
	}
}
