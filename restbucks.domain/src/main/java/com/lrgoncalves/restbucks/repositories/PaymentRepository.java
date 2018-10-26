package com.lrgoncalves.restbucks.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.domain.Payment;

public class PaymentRepository  implements GraphRepository<Payment>{

    private static final PaymentRepository theRepository = new PaymentRepository();
    private HashMap<String, Payment> backingStore = new HashMap<String, Payment>(); // Default implementation, not suitable for production!

    public static PaymentRepository current() {
        return theRepository;
    }
    
    private PaymentRepository(){}
    
    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#get(com.lrgoncalves.restbucks.domain.Identifier)
     */
    public Payment get(Identifier identifier) {
        return backingStore.get(identifier.toString());
    }
    
    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#take(com.lrgoncalves.restbucks.domain.Identifier)
     */
    public Payment take(Identifier identifier) {
        Payment payment = backingStore.get(identifier.toString());
        remove(identifier);
        return payment;
    }

    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#store(java.lang.Object)
     */
    public Identifier store(Payment payment) {
        Identifier id = new Identifier();
        backingStore.put(id.toString(), payment);
        return id;
    }
    
    /*
     * (non-Javadoc)
     * @see com.lrgoncalves.restbucks.repositories.GraphRepository#store(com.lrgoncalves.restbucks.domain.Identifier, java.lang.Object)
     */
    public void store(Identifier paymentIdentifier, Payment payment) {
        backingStore.put(paymentIdentifier.toString(), payment);
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
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, Payment> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<String, Payment>();
    }
}
