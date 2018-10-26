/**
 * 
 */
package com.lrgoncalves.service.order;

import com.lrgoncalves.restbucks.activities.*;
import com.lrgoncalves.restbucks.http.PATCH;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.representations.RestbucksUri;
import com.lrgoncalves.restbucks.resources.AbstractResource;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import static com.lrgoncalves.restbucks.representations.Representation.RESTBUCKS_MEDIA_TYPE_JSON;


/**
 * @author lrgoncalves
 *
 */
@Path("/order")
public class OrderResource extends AbstractResource{

	private @Context UriInfo uriInfo;

	@Inject
	private CreateOrderActivity createOrderActivity;
	
	@Inject
	private ReadOrderActivity readOrderActivity;
	
	@Inject
	private UpdateOrderActivity updateOrderActivity;

	@Inject
	private RemoveOrderActivity removeOrderActivity;

	public OrderResource() {
		super(OrderResource.class);
	}

	/**
	 * Used in test cases only to allow the injection of a mock UriInfo.
	 * 
	 * @param uriInfo
	 */
	public OrderResource(UriInfo uriInfo) {
		super(OrderResource.class);
		this.uriInfo = uriInfo;  
	}

	@GET
	@Path("/{orderId}/format/json")
	@Produces(RESTBUCKS_MEDIA_TYPE_JSON)
	public Response getOrderJson() {
		try {
			OrderRepresentation responseRepresentation = readOrderActivity.retrieveByUri(new RestbucksUri(uriInfo.getRequestUri()));
			return Response.ok().entity(responseRepresentation).type(MediaType.APPLICATION_JSON).build();
		} catch(NoSuchOrderException nsoe) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception ex) {
			return Response.serverError().build();
		}
	}



	@POST
	@Consumes(RESTBUCKS_MEDIA_TYPE_JSON)
	@Produces(RESTBUCKS_MEDIA_TYPE_JSON)
	public Response createJsonOrder(String orderRepresentation) {
		try {

			OrderRepresentation responseRepresentation = createOrderActivity
					.create(OrderRepresentation.fromJsonString(orderRepresentation).getOrder(),
							new RestbucksUri(uriInfo.getRequestUri()));


			return Response.created(responseRepresentation.getHypermedia().getUpdateLink(RESTBUCKS_MEDIA_TYPE_JSON).getUri()).entity(responseRepresentation).type(MediaType.APPLICATION_JSON).build();
		} catch (InvalidOrderException ioe) {
			return Response.status(Status.BAD_REQUEST).build();
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
            OrderRepresentation responseRepresentation = updateOrderActivity.update(OrderRepresentation.fromJsonString(orderRepresentation).getOrder(), new RestbucksUri(uriInfo.getRequestUri()));
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
	
	@DELETE
	@Path("/{orderId}/format/json")
	@Produces(RESTBUCKS_MEDIA_TYPE_JSON)
	public Response removeJsonOrder() {
		try {
			OrderRepresentation removedOrder = removeOrderActivity.delete(new RestbucksUri(uriInfo.getRequestUri()));
			return Response.ok().entity(removedOrder).type(MediaType.APPLICATION_JSON).build();
		} catch (NoSuchOrderException nsoe) {
			return Response.status(Status.NOT_FOUND).build();
		} catch(OrderDeletionException ode) {
			return Response.status(405).header("Allow", "GET").build();
		} catch (Exception ex) {
			return Response.serverError().build();
		}
	}

}
