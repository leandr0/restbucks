package com.lrgoncalves.product.catalog.service.repositories;

import static com.lrgoncalves.product.catalog.service.domain.ProductBuilder.product;
import static com.lrgoncalves.product.catalog.service.domain.PromotionBuilder.promotion;

import com.lrgoncalves.product.catalog.service.repositories.EventStore;

public class EventStoreBuilder {
    public static EventStoreBuilder eventStore() {
        return new EventStoreBuilder();
    }
    
    private EventStoreBuilder(){}
    
    public EventStoreBuilder withRandomEvents(int numberOfEvents) {
        for(int i = 0; i < numberOfEvents; i ++) {
            if(System.currentTimeMillis() % 2 == 0) {
                EventStore.current().store(product().createdAtRandomRecentDate().build());
            } else {
                EventStore.current().store(promotion().createdAtRandomRecentDate().build());
            }
        }
        return this;
    }
}
