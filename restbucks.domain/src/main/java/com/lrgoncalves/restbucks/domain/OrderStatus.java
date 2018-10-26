package com.lrgoncalves.restbucks.domain;

import javax.xml.bind.annotation.XmlEnumValue;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties
public enum OrderStatus {
    @XmlEnumValue(value="unpaid")
    UNPAID,
    @XmlEnumValue(value="preparing")
    PREPARING, 
    @XmlEnumValue(value="ready")
    READY, 
    @XmlEnumValue(value="taken")
    TAKEN,
    @XmlEnumValue(value="cancelled")
    CANCELLED;
    
    
    public static final String LABEL = "Status";
}
