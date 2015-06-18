package com.lrgoncalves.restbucks.resources;

import static com.lrgoncalves.restbucks.representations.Representation.RESTBUCKS_MEDIA_TYPE_JSON;
import static com.lrgoncalves.restbucks.representations.Representation.RESTBUCKS_MEDIA_TYPE_XML;
import static com.lrgoncalves.restbucks.representations.Representation.SELF_REL_VALUE;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.lrgoncalves.restbucks.activities.InvalidPaymentException;
import com.lrgoncalves.restbucks.activities.NoSuchOrderException;
import com.lrgoncalves.restbucks.activities.PaymentActivity;
import com.lrgoncalves.restbucks.activities.UpdateException;
import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.representations.Link;
import com.lrgoncalves.restbucks.representations.PaymentRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;


@Path("/payment/{paymentId}")
public class PaymentResource {
    
    private @Context UriInfo uriInfo;
    
    public PaymentResource(){}
    
    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * @param uriInfo
     */
    public PaymentResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @PUT
    @Consumes(RESTBUCKS_MEDIA_TYPE_XML)
    @Produces(RESTBUCKS_MEDIA_TYPE_XML)
    public Response pay(PaymentRepresentation paymentRepresentation) {
        try {
            return Response.created(uriInfo.getRequestUri()).entity(
                    new PaymentActivity().pay(paymentRepresentation.getPayment(), 
                            new RestbucksUri(uriInfo.getRequestUri()))).build();
        } catch(NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            Identifier identifier = new RestbucksUri(uriInfo.getRequestUri()).getId();
            Link link = new Link(SELF_REL_VALUE, new RestbucksUri(uriInfo.getBaseUri().toString() + "order/" + identifier));
            return Response.status(Status.FORBIDDEN).entity(link).build();
        } catch(InvalidPaymentException ipe) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch(Exception e) {
            return Response.serverError().build();
        }
    }
    
    @PUT
    @Consumes(RESTBUCKS_MEDIA_TYPE_JSON)
    @Produces(RESTBUCKS_MEDIA_TYPE_JSON)
    public Response payJsonOrder(PaymentRepresentation paymentRepresentation) {
        try {
        	
            return Response.created(uriInfo.getRequestUri()).entity(
                    new PaymentActivity().pay(paymentRepresentation.getPayment(), 
                            new RestbucksUri(uriInfo.getRequestUri()))).build();
        } catch(NoSuchOrderException nsoe) {
            return Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            Identifier identifier = new RestbucksUri(uriInfo.getRequestUri()).getId();
            Link link = new Link(SELF_REL_VALUE, new RestbucksUri(uriInfo.getBaseUri().toString() + "order/" + identifier));
            return Response.status(Status.FORBIDDEN).entity(link).build();
        } catch(InvalidPaymentException ipe) {
            return Response.status(Status.BAD_REQUEST).build();
        } catch(Exception e) {
            return Response.serverError().build();
        }
    }
}