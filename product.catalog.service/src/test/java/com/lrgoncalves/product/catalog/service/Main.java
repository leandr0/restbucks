package com.lrgoncalves.product.catalog.service;

import java.net.URI;
import java.util.HashMap;

import com.lrgoncalves.product.catalog.service.repositories.EventStore;
import com.lrgoncalves.product.catalog.service.repositories.EventStoreBuilder;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Must provide server URI as the only command line argument ");
            System.exit(1);
        } else {
            System.out.println("Starting server at: " + args[0]);
        }
        
        int numberOfEvents = 1111;
        EventStoreBuilder.eventStore().withRandomEvents(numberOfEvents);
        System.out.println(String.format("The product catalog contains [%d] events.", EventStore.current().getNumberOfEvents()));
        
        URI feedUri = new URI(args[0]);
        
        final HashMap<String, String> initParams = new HashMap<String, String>();
        
        initParams.put("com.sun.jersey.config.property.packages", "com.lrgoncalves.product.catalog.service.resources");

        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(feedUri, initParams);
        
        System.out.println("Press a key to terminate service");
        System.in.read();
        
        threadSelector.stopEndpoint();
        
        System.out.println("Service terminated.");
    }
}
