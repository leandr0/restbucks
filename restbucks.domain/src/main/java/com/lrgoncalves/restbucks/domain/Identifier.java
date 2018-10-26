package com.lrgoncalves.restbucks.domain;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public final class Identifier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5410833594843896424L;
	
	@JsonIgnore
	private final String identifier;

    public Identifier(String identifier) {
        this.identifier = identifier;
    }
    
    public Identifier() {
        this(java.util.UUID.randomUUID().toString());
    }    
    
    @JsonIgnore
	public String toString() {
        return identifier;
    }
}
