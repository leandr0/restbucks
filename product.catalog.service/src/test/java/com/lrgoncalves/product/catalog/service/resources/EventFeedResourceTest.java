package com.lrgoncalves.product.catalog.service.resources;

import static com.lrgoncalves.product.catalog.service.repositories.EventStoreBuilder.eventStore;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

import com.lrgoncalves.product.catalog.service.repositories.EventStore;
//import com.sun.jersey.impl.MultivaluedMapImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

public class EventFeedResourceTest {

    @Before
    public void clearTheEventStore() {
        EventStore.current().clear();
    }

    @Test
    public void should404OnInvalidUri() throws Exception {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("startPos", "0");
        final String badEndPos = "27";
        map.add("endPos", badEndPos);

        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getPathParameters()).thenReturn(map);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/0,27"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getSpecificFeed(0, 27);

        assertEquals(404, response.getStatus());
    }

    @Test
    public void should200ForAValidUri() throws Exception {
        eventStore().withRandomEvents(101);

        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("startPos", "0");
        final String godEndPos = "19";
        map.add("endPos", godEndPos);

        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getPathParameters()).thenReturn(map);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/0,19"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getSpecificFeed(0, 19);

        assertEquals(200, response.getStatus());
    }

    @Test
    public void shouldContainEntriesWhenEventStoreIsNotEmpty() throws Exception {
        eventStore().withRandomEvents(101);

        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("startPos", "20");
        final String goodEndPos = "39";
        map.add("endPos", goodEndPos);

        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getPathParameters()).thenReturn(map);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/20,39"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getSpecificFeed(20,39);

        SyndFeed f = toAtomFeed((String) response.getEntity());

        assertEquals(20, f.getEntries().size());
    }

    private SyndFeed toAtomFeed(String string) {
        SyndFeedInput input = new SyndFeedInput();
        try {
            return input.build(new StringReader(string));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldGet200WhenRequestingRecentFeedWhichIsEmpty() throws Exception {
        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/recent"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getRecentFeed();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void shouldGetEntriesWhenRequestingRecentFeedWhichIsNonEmpty() throws Exception {
        eventStore().withRandomEvents(20);

        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/recent"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getRecentFeed();

        SyndFeed f = toAtomFeed((String) response.getEntity());

        assertEquals(20, f.getEntries().size());

    }
    
    @Test
    public void shouldGetEntriesWhenRequestingWorkingFeedWhichIsNonEmptyButIncompleteInTermsOfPaging() throws Exception {
        eventStore().withRandomEvents(123);

        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/recent"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getRecentFeed();

        SyndFeed f = toAtomFeed((String) response.getEntity());

        assertEquals(3, f.getEntries().size());

    }
    
    @Test
    public void recentFeedShouldBeCachedForOneHour() throws Exception {
        eventStore().withRandomEvents(123);
        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/recent"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getRecentFeed();
        
        MultivaluedMap<String,Object> metadata = response.getMetadata();
        
        List<Object> matchingHeaders = metadata.get("Cache-Control");
        assertNotNull(matchingHeaders);
        assertEquals(1, matchingHeaders.size());
        String header = (String)matchingHeaders.get(0);
        assertThat(header, containsString("max-age=3600"));
    }
    
    @Test
    public void olderFeedsShouldBeCachedForOneMonth() throws Exception {
        eventStore().withRandomEvents(123);
        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/recent"));

        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getSpecificFeed(0, 19);
        
        MultivaluedMap<String,Object> metadata = response.getMetadata();
        
        List<Object> matchingHeaders = metadata.get("Cache-Control");
        assertNotNull(matchingHeaders);
        assertEquals(1, matchingHeaders.size());
        String header = (String)matchingHeaders.get(0);
        assertThat(header, containsString("max-age=2592000"));
    }
    
    @Test
    public void shouldReturnWorkingFeedForRecentEventsEvenIfNavigatedToViaSpecificUri() throws Exception {
        eventStore().withRandomEvents(123);
        UriInfo mockUriInfo = mock(UriInfo.class);
        when(mockUriInfo.getRequestUri()).thenReturn(new URI("http://restbucks.com/product-catalog/notifications/120,139"));
        
        EventFeedResource resource = new EventFeedResource(mockUriInfo);
        Response response = resource.getSpecificFeed(120, 139);
        
        MultivaluedMap<String,Object> metadata = response.getMetadata();
        
        List<Object> matchingHeaders = metadata.get("Cache-Control");
        assertNotNull(matchingHeaders);
        assertEquals(1, matchingHeaders.size());
        String header = (String)matchingHeaders.get(0);
        assertThat(header, containsString("max-age=3600")); // Working feed always caches for 1 hour only
    }
}
