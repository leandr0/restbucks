package com.lrgoncalves.restbucks.domain;

import static com.lrgoncalves.restbucks.representations.Representation.RELATIONS_URI;
import static com.lrgoncalves.restbucks.representations.Representation.RESTBUCKS_MEDIA_TYPE_JSON;
import static com.lrgoncalves.restbucks.representations.Representation.RESTBUCKS_MEDIA_TYPE_XML;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.lrgoncalves.restbucks.representations.Link;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

@JsonSerialize(include =Inclusion.NON_EMPTY)
public class Hypermedia {


	public static final String CANCEL_LINK	= "cancelLink";
	public static final String UPDATE_LINK	= "updateLink";
	public static final String PAYMENT_LINK	= "paymentLink";
	public static final String SELF_LINK	= "selfLink";
	public static final String RECIEPT_LINK	= "selfLink";
	public static final String ORDER_LINK	= "selfLink";

	protected List<Link> cancelLink 	= new LinkedList<Link>();
	protected List<Link> updateLink 	= new LinkedList<Link>();
	protected List<Link> selfLink 		= new LinkedList<Link>();
	protected List<Link> paymentLink 	= new LinkedList<Link>();
	protected List<Link> receiptLink 	= new LinkedList<Link>();
	protected List<Link> orderLink 		= new LinkedList<Link>();
	
	private static final String GET_FORMAT_JSON = "/format/json";
	private static final String GET_FORMAT_XML  = "/format/xml";

	/*For Json construtor*/
	public Hypermedia(){}

	public Hypermedia(Link cancelLink,Link updateLink,Link selfLink,Link paymentfLink){

		createHypermedia(this.cancelLink	, cancelLink);
		createHypermedia(this.updateLink	, updateLink);
		createHypermedia(this.paymentLink	, paymentfLink);
		createGetMethodLink(this.selfLink	, selfLink);
	}

	private void createHypermedia(List<Link> list, Link link){

		if(list != null && link != null){
			list.add(new Link(link.getRel(), new RestbucksUri(link.getUri()), RESTBUCKS_MEDIA_TYPE_XML));
			list.add(new Link(link.getRel(), new RestbucksUri(link.getUri()), RESTBUCKS_MEDIA_TYPE_JSON));
		}
	}

	private void createGetMethodLink(List<Link> list, Link link){

		if(list != null && link != null){
			list.add(new Link(link.getRel(), new RestbucksUri(link.getUri()+GET_FORMAT_JSON), RESTBUCKS_MEDIA_TYPE_JSON));
			list.add(new Link(link.getRel(), new RestbucksUri(link.getUri()+GET_FORMAT_XML), RESTBUCKS_MEDIA_TYPE_XML));
		}

	}

	public Hypermedia createRecieptLink(RestbucksUri requestUri){

		addRecieptLink(requestUri);

		this.paymentLink.clear();
		this.selfLink.clear();
		this.updateLink.clear();
		this.cancelLink.clear();

		return this;
	}

	public Hypermedia addRecieptLink(RestbucksUri requestUri){

		this.receiptLink.add(new Link(RELATIONS_URI, requestUri, RESTBUCKS_MEDIA_TYPE_JSON));
		this.receiptLink.add(new Link(RELATIONS_URI, requestUri, RESTBUCKS_MEDIA_TYPE_XML));
		
		return this;
	}
	
	public Hypermedia addSelfLink(RestbucksUri requestUri){
		
		createHypermedia(this.selfLink, new Link(RELATIONS_URI, requestUri));
		
		return this;
	}

	public Hypermedia create(String hypermediaType){

		switch (hypermediaType) {
		case CANCEL_LINK:
			this.paymentLink.clear();
			this.selfLink.clear();
			this.updateLink.clear();
			break;
		case PAYMENT_LINK:
			this.selfLink.clear();
			this.updateLink.clear();
			this.cancelLink.clear();
			break;

		case SELF_LINK:
			this.paymentLink.clear();
			this.updateLink.clear();
			this.cancelLink.clear();
			break;

		case UPDATE_LINK:
			this.paymentLink.clear();
			this.selfLink.clear();
			this.cancelLink.clear();
			break;
		}

		return this;
	}


	public List<Link> getCancelLink() {
		return cancelLink;
	}

	public List<Link> getUpdateLink() {
		return updateLink;
	}

	public List<Link> getSelfLink() {
		return selfLink;
	}

	public List<Link> getPaymentLink() {
		return paymentLink;
	}

	public Link getCancelLink(String mediaType) {
		return getLink(cancelLink , mediaType);
	}

	public Link getUpdateLink(String mediaType) {
		return getLink(updateLink , mediaType);
	}

	public Link getSelfLink(String mediaType) {
		return getLink(selfLink , mediaType);
	}

	public Link getPaymentfLink(String mediaType) {
		return getLink(paymentLink , mediaType);
	}

	public List<Link> getOrderLink() {
		return orderLink;
	}

	public List<Link> getReceiptLink() {
		return receiptLink;
	}

	private Link getLink(List<Link> links, String mediaType){

		for(Link link : links ){
			if(link.getMediaType()== mediaType){
				return link;
			}
		}

		throw new IllegalArgumentException("Link not found");
	}
}