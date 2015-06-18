package com.lrgoncalves.restbucks.representations;

import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.lrgoncalves.restbucks.domain.Hypermedia;

@JsonSerialize(include =Inclusion.NON_EMPTY)
public abstract class Representation {

	public static final String RELATIONS_URI 				= "http://relations.restbucks.com/";
	public static final String RESTBUCKS_NAMESPACE 			= "http://schemas.restbucks.com";
	public static final String DAP_NAMESPACE 				= RESTBUCKS_NAMESPACE + "/dap";
	public static final String RESTBUCKS_MEDIA_TYPE_XML		= "application/vnd.restbucks+xml";
	public static final String RESTBUCKS_MEDIA_TYPE_JSON 	= "application/vnd.restbucks+json";
	public static final String SELF_REL_VALUE 				= "self";
	public static final String NAME 						= "order";
	public static final String FORMAT_JSON_PATH				= "/format/json";
	public static final String FORMAT_XML_PATH				= "/format/xml";

	@XmlElement(name = "hypermedia", namespace = DAP_NAMESPACE)
	@JsonProperty
	protected Hypermedia hypermedia;

	@JsonIgnore
	protected static final ObjectMapper jsonMapper = new ObjectMapper();
	
	public Hypermedia getHypermedia() {
		return hypermedia;
	}
	
	
}
