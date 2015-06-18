package com.lrgoncalves.product.catalog.service.repositories;

import static com.lrgoncalves.product.catalog.service.repositories.EventStoreBuilder.eventStore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lrgoncalves.product.catalog.service.domain.Event;

public class EventStoreTest {
    
    private int numberOfEvents;

    @Before
    public void fillEventStoreWithProductsAndPromotions() {
    	EventStore.current().clear();
        numberOfEvents = 100;
        eventStore().withRandomEvents(numberOfEvents);
    }
   
    
    @Test
    public void shouldBeAbleToRetrieveSpecifiedEvents() {
        List<Event> events = EventStore.current().getEvents(20, 20);
        assertEquals(20, events.size());
                
        events = EventStore.current().getEvents(90, 500);
        assertEquals(10, events.size());
    }
    
    @Test
    public void eventsShouldBeOrderedFromOldestToNewest() {
        List<Event> events = EventStore.current().getEvents(0, 100);
        
        for(int i = 0; i < numberOfEvents -1; i ++) {
            final int comparisonResult = events.get(i).compareTo(events.get(i+1));
            assertTrue(comparisonResult == -1 || comparisonResult == 0); // Earlier or same day.
        }
    }
}