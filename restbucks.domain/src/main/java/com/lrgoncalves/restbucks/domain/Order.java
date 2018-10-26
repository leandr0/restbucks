package com.lrgoncalves.restbucks.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = { "location" , "identifier","status"})
public class Order implements Serializable{
   

	/**
	 * 
	 */
	private static final long serialVersionUID = -607787994746887454L;

	public static final String LABEL = "Order";

    private Location location;
	
    private List<Item> items;
   
    @JsonIgnore
    private Identifier identifier;
    
    private OrderStatus status = OrderStatus.UNPAID;

    public Order(){
    	location = Location.IN_STORE;
        items = null;
    }
    
    public Order(Identifier identifier){
    	this.identifier = identifier;
    	location = Location.IN_STORE;
        items = null;
    }
    
    
    public Order(Location location, List<Item> items) {
      this(location, OrderStatus.UNPAID, items);
    }
    

    public Order(Location location, OrderStatus status, List<Item> items) {
        this.location = location;
        this.items = items;
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
		this.location = location;
	}

	public List<Item> getItems() {
        return items;
    }

    public double calculateCost() {
        double total = 0.0;
        if (items != null) {
            for (Item item : items) {
                if(item != null && item.getDrink() != null) {
                    total += item.getDrink().getPrice();
                }
            }
        }
        return total;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public OrderStatus getStatus() {
        return status;
    }
    
    @JsonProperty
    public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}
    
    @JsonIgnore
    public Identifier getIdentifier() {
		return identifier;
	}

    public void addItem(final Item item){
    	
    	if(item == null){
    		throw new IllegalArgumentException("Invalid parameter");
    	}
    	
    	if(items == null){
    		this.items = new LinkedList<Item>();
    	}
    	
    	items.add(item);
    }
    
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Location: " + location + "\n");
        sb.append("Status: " + status + "\n");
        for(Item i : items) {
            sb.append("Item: " + i.toString()+ "\n");
        }
        return sb.toString();
    }
}