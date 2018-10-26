/**
 * 
 */
package com.lrgoncalves.restbucks.activities;

import static com.lrgoncalves.restbucks.representations.Representation.RELATIONS_URI;
import static com.lrgoncalves.restbucks.representations.Representation.SELF_REL_VALUE;

import com.lrgoncalves.restbucks.domain.Hypermedia;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.representations.Link;
import com.lrgoncalves.restbucks.representations.Representation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

/**
 * @author lrgoncalves
 *
 */
public class HypermediaActivity {

	public static Hypermedia create(Identifier identifier , RestbucksUri requestUri){

		RestbucksUri 	orderUri 			= getOrderUri(requestUri, identifier);
		RestbucksUri	paymentUri 			= getPaymentUrl(requestUri, identifier);
		String 			url 				= getUrl(requestUri);
		
		return new Hypermedia(
				new Link(RELATIONS_URI + "cancel", url,"cancel"),
				new Link(RELATIONS_URI + "order", url,"order"), 
				new Link(SELF_REL_VALUE, orderUri), 
				new Link(RELATIONS_URI + "payment", paymentUri));

	}

	private static RestbucksUri getPaymentUrl(RestbucksUri requestUri,Identifier identifier){
		String 			url 				= getUrl(requestUri);
		RestbucksUri 	orderUri 			= getOrderUri(requestUri, identifier);
		RestbucksUri	paymentUri 			= new RestbucksUri(url+ "payment/" + orderUri.getId().toString());

		return paymentUri;
	}

	private static String getUrl(RestbucksUri requestUri){

		if(!requestUri.getFullUri().toString().endsWith("/")){
			return requestUri.getFullUri().toString().replaceAll("order.*", "");
		}

		return requestUri.getFullUri().toString().replaceAll("order/.*", "");
	}

	private static RestbucksUri getOrderUri(RestbucksUri requestUri,Identifier identifier){

		return new RestbucksUri(getUrl(requestUri).concat("order/") + identifier.toString());

	}

	public static Hypermedia create(RestbucksUri requestUri){

		Identifier identifier = requestUri.getId();
		RestbucksUri paymentUri = getPaymentUrl(requestUri, identifier); 

		String url = getUrl(requestUri);

		return new Hypermedia(
				new Link(RELATIONS_URI + "cancel", url,"cancel"),
				new Link(RELATIONS_URI + "order", url,"order"), 
				new Link(Representation.SELF_REL_VALUE, getOrderUri(requestUri, identifier)), 
				new Link(RELATIONS_URI + "payment", paymentUri));
	}

	public static Hypermedia  createRecieptLink(RestbucksUri requestUri){
		return createOrderForReciept(requestUri);
	}

	public static Hypermedia createForPayment(RestbucksUri requestUri){

		return new Hypermedia(null,null,
				new Link(Representation.RELATIONS_URI + "order", UriExchange.orderForPayment(requestUri)),
				null).addRecieptLink(UriExchange.receiptForPayment(requestUri));

	}

	public static Hypermedia createOrderForReciept(RestbucksUri requestUri){

		return new Hypermedia().addRecieptLink(requestUri).addSelfLink(UriExchange.orderForReceipt(requestUri));//UriExchange.orderForReceipt(requestUri));
	}

}