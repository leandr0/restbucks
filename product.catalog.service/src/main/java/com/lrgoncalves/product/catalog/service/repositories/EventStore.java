package com.lrgoncalves.product.catalog.service.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lrgoncalves.product.catalog.service.domain.Event;

/**
 * This is a great way to build a database that doesn't scale!
 * This shouldn't be used for a production environment, use a real data store instead.
 */
public class EventStore {
	private static EventStore theStore = null;
	
	public static synchronized EventStore current() {
		if(theStore == null) {
			theStore = new EventStore();
		}
		
		return theStore;
	}
	
	private ArrayList<Event> events = new ArrayList<Event>(); 
	
	private EventStore(){}
	
    public void store(Event event) {
        events.add(event);
        Collections.sort(events);
    }

    public List<Event> getEvents(int first, int numberOfEvents) {
        if(first < 0) {
            first = 0;
        }

        if(first + numberOfEvents > events.size()) {
            numberOfEvents = events.size() - first;
        }
        
        return events.subList(first, first + numberOfEvents); // subList is exclusive, we want inclusive
    }

    public synchronized void clear() {
        theStore = null;
    }

    public int getNumberOfEvents() {
        return events.size();
    }
}
