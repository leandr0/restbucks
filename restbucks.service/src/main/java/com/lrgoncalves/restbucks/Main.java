package com.lrgoncalves.restbucks;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.lrgoncalves.restbucks.activities.HypermediaActivity;
import com.lrgoncalves.restbucks.domain.Drink;
import com.lrgoncalves.restbucks.domain.Hypermedia;
import com.lrgoncalves.restbucks.domain.Item;
import com.lrgoncalves.restbucks.domain.Location;
import com.lrgoncalves.restbucks.domain.Milk;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.OrderStatus;
import com.lrgoncalves.restbucks.domain.Payment;
import com.lrgoncalves.restbucks.domain.Size;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.PaymentRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;
import com.sun.research.ws.wadl.Link;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Main {

	public static void main(String[] args) throws IOException {

		try{

			//createOrder();
			payOrder();
			//createOrderJson();
			/*
			String path = "http://localhost:8080/restbucks-service/service/order/7f1a1b49-67f0-4d50-95ec-17759c87b10f/format/json";
			path = path.replaceAll("/format/.*", "");
			System.out.println(path.substring(path.lastIndexOf("/") + 1, path.length()));
			 */
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private static void payOrder() throws JAXBException, JsonGenerationException, JsonMappingException, IOException {

		Payment payment = new Payment(2.0, "LEANDRO R GONCALVES", "9999 9999 9999 9999", 05, 2023);

		com.lrgoncalves.restbucks.representations.Link		self 	= new com.lrgoncalves.restbucks.representations.Link();
		com.lrgoncalves.restbucks.representations.Link[] 	links 	= new com.lrgoncalves.restbucks.representations.Link[1];

		links[0] = self;

		com.lrgoncalves.restbucks.representations.Link	 paymentLink = 
				new com.lrgoncalves.restbucks.representations.Link("http://relations.restbucks.com/payment",
						new RestbucksUri("http://localhost:8080/restbucks-service/service/payment/1d4ba500-0b96-46e9-acb4-a129d50199b1"));
	
		Hypermedia hypermedia = new Hypermedia(null,null,null,paymentLink); //HypermediaActivity.createForPayment(requestUri);
		
		//hypermedia.
		
		PaymentRepresentation paymentRepresentation = new PaymentRepresentation(payment, hypermedia);//new PaymentRepresentation(payment, links);

		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println(mapper.writeValueAsString(paymentRepresentation));
		
		/*JAXBContext	context 	= JAXBContext.newInstance(PaymentRepresentation.class);
		Marshaller 	marshaller 	= context.createMarshaller();

		marshaller.marshal(paymentRepresentation, new File("C:\\workspace\\sources\\RESTinPractice\\payment.xml"));*/
	}


	private static void createOrder() throws JAXBException {

		Location 	location 	= Location.TAKEAWAY;
		List<Item> 	items 		= new LinkedList<Item>();
		Item 		itemA 		= new Item(Size.MEDIUM, Milk.SEMI, Drink.CAPPUCCINO);
		Item 		itemB 		= new Item(Size.MEDIUM, Milk.NONE, Drink.ESPRESSO);

		items.add(itemA);
		items.add(itemB);

		Order 	order 	= new Order(location, items);
		XStream xstream = new XStream(new StaxDriver());
		//new XStream(new DomDriver()); 
		//new XStream();

		xstream.alias("order", Order.class);
		xstream.alias("item", Item.class);
		xstream.alias("items", LinkedList.class);

		//String xmlRepresentation = xstream.toXML(order);

		//System.out.println(xmlRepresentation);

		Link 	self 	= new Link();
		Link[] 	links 	= new Link[1];

		links[0] = self;

		OrderRepresentation orderRepresentation = new OrderRepresentation();//(order, links);
		RestbucksUri		restbucksUri 		= new RestbucksUri("http://localhost:8080/restbucks-service/service/");
		//http://localhost:8080/restbucks-service/service/order

		orderRepresentation = orderRepresentation.createResponseOrderRepresentation(order, restbucksUri);

		System.out.println(xstream.toXML(orderRepresentation));

		JAXBContext	context 	= JAXBContext.newInstance(OrderRepresentation.class);
		Marshaller 	marshaller 	= context.createMarshaller();

		marshaller.marshal(orderRepresentation, new File("C:\\workspace\\sources\\RESTinPractice\\order.xml"));

		/*Unmarshaller unmarshaller = context.createUnmarshaller();
        OrderRepresentation or = (OrderRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));*/

	}

	private static void createOrderJson() throws JAXBException, JsonGenerationException, JsonMappingException, IOException {

		Location 	location 	= Location.TAKEAWAY;
		List<Item> 	items 		= new LinkedList<Item>();
		Item 		itemA 		= new Item(Size.MEDIUM, Milk.SEMI, Drink.CAPPUCCINO);
		Item 		itemB 		= new Item(Size.MEDIUM, Milk.NONE, Drink.ESPRESSO);

		items.add(itemA);
		items.add(itemB);

		Order 	order 	= new Order(location, items);

		order.setStatus(OrderStatus.PREPARING);
		
		OrderRepresentation orderRepresentation = new OrderRepresentation();
		RestbucksUri		restbucksUri 		= new RestbucksUri("http://localhost:8080/restbucks-service/service/");

		orderRepresentation = orderRepresentation.createResponseOrderRepresentation(order, restbucksUri);

		ObjectMapper mapper = new ObjectMapper();
		
		String json = mapper.writeValueAsString(orderRepresentation);
		System.out.println(json);
		
		OrderRepresentation or = mapper.readValue(json, OrderRepresentation.class);
		
		System.out.println(or.getStatus().name());
		
		//System.out.println(xstream.toXML(orderRepresentation));

	}
}