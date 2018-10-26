package com.lrgoncalves.restbucks.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Order;

public class OrderRepository implements GraphRepository<Order>{

    private static final OrderRepository theRepository = new OrderRepository();
    private HashMap<String, Order> backingStore = new HashMap<String, Order>(); // Default implementation, not suitable for production!

    public static OrderRepository current() {
        return theRepository;
    }
    
    private OrderRepository(){}
    
    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#get(com.lrgoncalves.restbucks.domain.Identifier)
     */
    public Order get(Identifier identifier) {
        return backingStore.get(identifier.toString());
     }
    
    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#take(com.lrgoncalves.restbucks.domain.Identifier)
     */
    public Order take(Identifier identifier) {
        Order order = backingStore.get(identifier.toString());
        remove(identifier);
        return order;
    }

    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#store(java.lang.Object)
     */
    public Identifier store(Order order) {
        Identifier id = new Identifier();
        backingStore.put(id.toString(), order);
        return id;
    }
    
    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#store(com.lrgoncalves.restbucks.domain.Identifier, java.lang.Object)
     */
    public void store(Identifier orderIdentifier, Order order) {
        backingStore.put(orderIdentifier.toString(), order);
    }

    public boolean has(Identifier identifier) {
        boolean result =  backingStore.containsKey(identifier.toString());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#remove(com.lrgoncalves.restbucks.domain.Identifier)
     */
    public void remove(Identifier identifier) {
        backingStore.remove(identifier.toString());
    }
    
    public boolean orderPlaced(Identifier identifier) {
        return OrderRepository.current().has(identifier);
    }
    
    public boolean orderNotPlaced(Identifier identifier) {
        return !orderPlaced(identifier);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Order> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<String, Order>();
    }

    public int size() {
        return backingStore.size();
    }
}
