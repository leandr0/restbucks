package com.lrgoncalves.product.catalog.service.resources;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.lrgoncalves.product.catalog.service.feeds.EventFeedGenerator;
import com.lrgoncalves.product.catalog.service.repositories.EventStore;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.io.WireFeedOutput;

@Path("/product-catalog/notifications")
public class EventFeedResource {

    private static final int ENTRIES_PER_FEED = 20;

    private static final String ATOM_MEDIA_TYPE = "application/atom+xml";

    private static final String CACHE_CONTROL_HEADER = "Cache-Control";

    private @Context
    UriInfo uriInfo;

    public EventFeedResource() {
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public EventFeedResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;

    }

    @GET
    @Path("/recent")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response getRecentFeed() {
        EventFeedGenerator generator = new EventFeedGenerator(uriInfo.getRequestUri(), ENTRIES_PER_FEED);
        Feed feed = generator.getRecentFeed();
        
        return Response.ok().entity(stringify(feed)).header(CACHE_CONTROL_HEADER, cacheDirective(CachePolicy.getRecentFeedLifetime())).type(ATOM_MEDIA_TYPE).build();
    }
    
    private Response getWorkingFeed() {
        EventFeedGenerator generator = new EventFeedGenerator(uriInfo.getRequestUri(), ENTRIES_PER_FEED);
        Feed feed = generator.getWorkingFeed();
        
        return Response.ok().entity(stringify(feed)).header(CACHE_CONTROL_HEADER, cacheDirective(CachePolicy.getWorkingFeedLifetime())).type(ATOM_MEDIA_TYPE).build();
    }

    private String cacheDirective(int seconds) {
        return "max-age=" + seconds;
    }

    @GET
    @Path("/{startPos},{endPos}")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response getSpecificFeed(@PathParam("startPos") int startPos, @PathParam("endPos") int endPos) {

    	if (invalidStartAndEndEntries(startPos, endPos)) {
    		// Bad URI - the paramters don't align with our feeds
    		return Response.status(Status.NOT_FOUND).build();
    	}

    	if(workingFeedRequested(startPos)) {
            return getWorkingFeed();
        }
        
        EventFeedGenerator generator = new EventFeedGenerator(uriInfo.getRequestUri(), ENTRIES_PER_FEED);
        Feed feed = generator.getArchiveFeed(startPos);
        return Response.ok().entity(stringify(feed)).header(CACHE_CONTROL_HEADER, cacheDirective(CachePolicy.getArchiveFeedLifetime())).type(ATOM_MEDIA_TYPE).build();
    }

	private boolean invalidStartAndEndEntries(int startPos, int endPos) {
		return startPos % ENTRIES_PER_FEED != 0 || endPos != startPos + ENTRIES_PER_FEED - 1;
	}

    private boolean workingFeedRequested(int startPos) { 
        final int numberOfEvents = EventStore.current().getNumberOfEvents();
        return startPos <= numberOfEvents && numberOfEvents <= startPos + ENTRIES_PER_FEED;         
    }

    private String stringify(Feed feed) {
        try {
            WireFeedOutput output = new WireFeedOutput();
            final StringWriter stringWriter = new StringWriter();
            output.output(feed, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
