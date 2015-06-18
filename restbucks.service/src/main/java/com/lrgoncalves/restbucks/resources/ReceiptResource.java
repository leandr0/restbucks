package com.lrgoncalves.restbucks.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.lrgoncalves.restbucks.activities.CompleteOrderActivity;
import com.lrgoncalves.restbucks.activities.NoSuchOrderException;
import com.lrgoncalves.restbucks.activities.OrderAlreadyCompletedException;
import com.lrgoncalves.restbucks.activities.OrderNotPaidException;
import com.lrgoncalves.restbucks.activities.ReadReceiptActivity;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.ReceiptRepresentation;
import static com.lrgoncalves.restbucks.representations.Representation.*;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

@Path("/receipt")
public class ReceiptResource {

    private @Context
    UriInfo uriInfo;

    public ReceiptResource() {}

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public ReceiptResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;

    }

    @GET
    @Path("/{orderId}"+FORMAT_XML_PATH)
    @Produces(RESTBUCKS_MEDIA_TYPE_XML)
    public Response getReceipt() {
        try {
            ReceiptRepresentation responseRepresentation = new ReadReceiptActivity().read(new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(responseRepresentation).build();
        } catch (OrderAlreadyCompletedException oce) {
            return Response.status(Status.NO_CONTENT).build();
        } catch (OrderNotPaidException onpe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch (NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }//TODO hypermedia de receipt
    //TODO receipt e self são as mesmas urls
    
    @GET
    @Path("/{orderId}"+FORMAT_JSON_PATH)
    @Produces(RESTBUCKS_MEDIA_TYPE_JSON)
    public Response getReceiptJson() {
        try {
            ReceiptRepresentation responseRepresentation = new ReadReceiptActivity().read(new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(responseRepresentation).build();
        } catch (OrderAlreadyCompletedException oce) {
            return Response.status(Status.NO_CONTENT).build();
        } catch (OrderNotPaidException onpe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch (NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @DELETE
    @Path("/{orderId}")
    @Produces(RESTBUCKS_MEDIA_TYPE_XML)
    public Response completeOrder(@PathParam("orderId")String identifier) {
        try {
            OrderRepresentation finalizedOrderRepresentation = new CompleteOrderActivity().completeOrder(new Identifier(identifier));
            return Response.ok().entity(finalizedOrderRepresentation).build();
        } catch (OrderAlreadyCompletedException oce) {
            return Response.status(Status.NO_CONTENT).build();
        } catch (NoSuchOrderException nsoe) {
                return Response.status(Status.NOT_FOUND).build();
        } catch (OrderNotPaidException onpe) {
            return Response.status(Status.CONFLICT).build();
        }
    }
    
    @DELETE
    @Path("/{orderId}")
    @Produces(RESTBUCKS_MEDIA_TYPE_JSON)
    public Response completeJsonOrder(@PathParam("orderId")String identifier) {
        try {
            OrderRepresentation finalizedOrderRepresentation = new CompleteOrderActivity().completeOrder(new Identifier(identifier));
            return Response.ok().entity(finalizedOrderRepresentation).build();
        } catch (OrderAlreadyCompletedException oce) {
            return Response.status(Status.NO_CONTENT).build();
        } catch (NoSuchOrderException nsoe) {
                return Response.status(Status.NOT_FOUND).build();
        } catch (OrderNotPaidException onpe) {
            return Response.status(Status.CONFLICT).build();
        }
    }
}