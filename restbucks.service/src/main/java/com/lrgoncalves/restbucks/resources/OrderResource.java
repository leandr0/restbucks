package com.lrgoncalves.restbucks.resources;

import static com.lrgoncalves.restbucks.representations.Representation.RESTBUCKS_MEDIA_TYPE_JSON;
import static com.lrgoncalves.restbucks.representations.Representation.RESTBUCKS_MEDIA_TYPE_XML;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.lrgoncalves.restbucks.activities.CreateOrderActivity;
import com.lrgoncalves.restbucks.activities.InvalidOrderException;
import com.lrgoncalves.restbucks.activities.NoSuchOrderException;
import com.lrgoncalves.restbucks.activities.OrderDeletionException;
import com.lrgoncalves.restbucks.activities.ReadOrderActivity;
import com.lrgoncalves.restbucks.activities.RemoveOrderActivity;
import com.lrgoncalves.restbucks.activities.UpdateException;
import com.lrgoncalves.restbucks.activities.UpdateOrderActivity;
import com.lrgoncalves.restbucks.http.PATCH;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;

@Path("/order")
public class OrderResource {

    private @Context UriInfo uriInfo;

    public OrderResource() {
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public OrderResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;  
    }
    
    @GET
    @Path("/{orderId}/format/xml")
    @Produces(RESTBUCKS_MEDIA_TYPE_XML)
    public Response getOrder() {
        try {
            OrderRepresentation responseRepresentation = new ReadOrderActivity().retrieveByUri(new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(responseRepresentation).build();
        } catch(NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("/{orderId}/format/json")
    @Produces(RESTBUCKS_MEDIA_TYPE_JSON)
    public Response getOrderJson() {
        try {
            OrderRepresentation responseRepresentation = new ReadOrderActivity().retrieveByUri(new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(responseRepresentation).build();
        } catch(NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }
    
    
    @POST
    @Consumes(RESTBUCKS_MEDIA_TYPE_XML)
    @Produces(RESTBUCKS_MEDIA_TYPE_XML)
    public Response createOrder(String orderRepresentation) {
        try {
            OrderRepresentation responseRepresentation = new CreateOrderActivity().create(OrderRepresentation.fromXmlString(orderRepresentation).getOrder(), new RestbucksUri(uriInfo.getRequestUri()));
            return Response.created(responseRepresentation.getHypermedia().getUpdateLink(RESTBUCKS_MEDIA_TYPE_XML).getUri()).entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }
    
    @POST
    @Consumes(RESTBUCKS_MEDIA_TYPE_JSON)
    @Produces(RESTBUCKS_MEDIA_TYPE_JSON)
    public Response createJsonOrder(String orderRepresentation) {
        try {
            OrderRepresentation responseRepresentation = new CreateOrderActivity()
            												.create(OrderRepresentation.fromJsonString(orderRepresentation).getOrder(),
            														new RestbucksUri(uriInfo.getRequestUri()));
            
            
            return Response.created(responseRepresentation.getHypermedia().getUpdateLink(RESTBUCKS_MEDIA_TYPE_JSON).getUri()).entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{orderId}/format/xml")
    @Produces(RESTBUCKS_MEDIA_TYPE_XML)
    public Response removeOrder() {
        try {
            OrderRepresentation removedOrder = new RemoveOrderActivity().delete(new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(removedOrder).build();
        } catch (NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch(OrderDeletionException ode) {
            return Response.status(405).header("Allow", "GET").build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{orderId}/format/json")
    @Produces(RESTBUCKS_MEDIA_TYPE_JSON)
    public Response removeJsonOrder() {
        try {
            OrderRepresentation removedOrder = new RemoveOrderActivity().delete(new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(removedOrder).build();
        } catch (NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch(OrderDeletionException ode) {
            return Response.status(405).header("Allow", "GET").build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }
    
    @PATCH
    @Path("/{orderId}")
    @Consumes(RESTBUCKS_MEDIA_TYPE_XML)
    @Produces(RESTBUCKS_MEDIA_TYPE_XML)
    public Response updateOrder(String orderRepresentation) {
        try {
            OrderRepresentation responseRepresentation = new UpdateOrderActivity().update(OrderRepresentation.fromXmlString(orderRepresentation).getOrder(), new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            return Response.status(Status.CONFLICT).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        } 
     }
    
    @PATCH
    @Path("/{orderId}")
    @Consumes(RESTBUCKS_MEDIA_TYPE_JSON)
    @Produces(RESTBUCKS_MEDIA_TYPE_JSON)
    public Response updateJsonOrder(String orderRepresentation) {
        try {
            OrderRepresentation responseRepresentation = new UpdateOrderActivity().update(OrderRepresentation.fromJsonString(orderRepresentation).getOrder(), new RestbucksUri(uriInfo.getRequestUri()));
            return Response.ok().entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch (NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            return Response.status(Status.CONFLICT).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        } 
     }
}