package com.lrgoncalves.product.catalog.service.domain;

import java.net.URI;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public abstract class Event implements Comparable<Event> {
	public static final String RESTBUCKS_NAMESPACE = "http://schemas.restbucks.com";
	
	private static int counter = 0;
	private static synchronized int getEventId() {
	    return counter++;
	}

	@XmlAttribute(name="href")
	private String uri;
	@XmlTransient
	protected DateTime updated;
	@XmlTransient
	private int eventId;
	@XmlTransient
	private String tagUri;
	@XmlTransient
	private boolean isUpdate;
	@XmlTransient
    private String createdBy;
		
    protected Event() {} // For JAXB :-(
    
    protected Event(URI uri, boolean isUpdate, DateTime timestamp, String createdBy) {
        this.isUpdate = isUpdate;
        this.createdBy = createdBy;
        this.eventId = getEventId();
        this.uri = uri.toString();
        this.updated = timestamp;
	}

    public String getTagUri() {
        if(tagUri == null) {
            tagUri = "tag:restbucks.com," + DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime()) + ":" + eventId;
        }
        return tagUri;
    }
    
	public int compareTo(Event update) {
	    if(updated.isAfter(update.updated)) {
	        return 1;
	    } else if (updated.isEqual(update.updated)) {
	        return 0;
	    } else {
	        return -1;
	    }
	}

	public int getId() {
	    return eventId;
	}
	
    public DateTime getUpdated() {
        return updated;
    }
    
    public void update() {
        updated = new DateTime();
    }
    

    public Date getTimestamp() {
        return updated.toDate();
    }

    public String getAssociatedUri() {
        return uri;
    }

    public String getEventStatus() {
        if(isUpdate) {
            return "update";
        } else {
            return "new";
        }
    }   

    public abstract String getEventType();
    public abstract String toXmlString();
}