package com.lrgoncalves.product.catalog.service.domain;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("unused")
@XmlRootElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
public class Link {
	@XmlAttribute(name="href")
	private URI uri;
	@XmlAttribute(name="rel")
	private String relValue;
	@XmlAttribute(name="type")
	private String mediaType;
	
	private Link(){} // For JAXB :-(
	
	public Link(URI uri, String relValue, String mediaType) {
		this.uri = uri;
		this.relValue = relValue;
		this.mediaType = mediaType;
	}

	public Link(URI uri, String mediaType) {
		this.uri = uri;
		this.mediaType = mediaType;
	}
}