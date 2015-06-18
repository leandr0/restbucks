package com.lrgoncalves.restbucks.representations;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@XmlRootElement(namespace = Representation.DAP_NAMESPACE)
@JsonSerialize(include =Inclusion.NON_NULL)
public class Link {
	
    @XmlAttribute(name = "rel")
    private String rel;
    
    @XmlAttribute(name = "uri")
    private String uri;

    @XmlAttribute(name = "mediaType")
    @JsonSerialize(include =Inclusion.NON_NULL)
    private String mediaType;

    /**
     * For JAXB :-(
     */
    public Link() {
    }

    public Link(String rel, RestbucksUri uri, String mediaType) {
        this.rel = rel;
        this.uri = uri.getFullUri().toString();
        this.mediaType = mediaType;

    }

    public Link(String rel, RestbucksUri uri) {
        this(rel, uri, Representation.RESTBUCKS_MEDIA_TYPE_XML);
    }

    public Link(String rel, String url,String urn, String mediaType) {
    	this.rel = rel;
        this.uri = url+urn;
        this.mediaType = mediaType;
    }
    
    public Link(String rel, String url,String urn) {
    	this.rel = rel;
        this.uri = url+urn;
    }
    
    public String getRel() {
        return rel;
    }

	public URI getUri() {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMediaType() {
        return mediaType;
    }
}
