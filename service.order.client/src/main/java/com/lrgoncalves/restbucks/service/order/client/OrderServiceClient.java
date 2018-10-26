package com.lrgoncalves.restbucks.service.order.client;

import java.net.URL;

import javax.inject.Named;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lrgoncalves.restbucks.domain.Drink;
import com.lrgoncalves.restbucks.domain.Item;
import com.lrgoncalves.restbucks.domain.Milk;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.Size;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;

@Named
public class OrderServiceClient {

	static final HttpTransport 	HTTP_TRANSPORT 	= new NetHttpTransport();
	static final JsonFactory 	JSON_FACTORY 	= new JacksonFactory();
	static final String ORDER_SERVICE_ENDPOINT = "http://localhost:8080";

	//TODO Utilizar UDDI
	/*public OrderServiceClient(final String orderServiceEndpoint) {
		ORDER_SERVICE_ENDPOINT = orderServiceEndpoint;
	}*/

	public OrderRepresentation createOrder(OrderRepresentation orderRepresentation){

		OrderRepresentation orderRepresentationResponse = null;

		try{

			HttpRequestFactory requestFactory =
					HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
						@Override
						public void initialize(HttpRequest request) {
							request.setParser(new JsonObjectParser(JSON_FACTORY));
						}
					});


			GenericUrl url = new GenericUrl(new URL(ORDER_SERVICE_ENDPOINT+"/service-order/service/order"));

			//String requestBody = "{\"cost\":3.5,\"status\":\"UNPAID\",\"location\":\"TAKEAWAY\",\"items\":[{\"milk\":\"SEMI\",\"size\":\"MEDIUM\",\"drink\":\"CAPPUCCINO\"},{\"milk\":\"NONE\",\"size\":\"MEDIUM\",\"drink\":\"ESPRESSO\"},{\"milk\":\"NONE\",\"size\":\"MEDIUM\",\"drink\":\"ESPRESSO\"}]}";

			HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString(null, orderRepresentation.toString()));

			request.getHeaders().setContentType("application/vnd.restbucks+json");

			HttpResponse response = request.execute();

			orderRepresentationResponse = OrderRepresentation.fromJsonString(response.parseAsString());

		}catch(Exception ex){
			ex.printStackTrace();
		}

		return orderRepresentationResponse;
	}

	public static void main(String args[]){

		try{

			OrderServiceClient client = new OrderServiceClient();//"http://localhost:8080");

			Order order = new Order();

			order.addItem(new Item(Size.SMALL, Milk.NONE, Drink.ESPRESSO));

			order.addItem(new Item(Size.MEDIUM, Milk.NONE, Drink.CAPPUCCINO));

			OrderRepresentation representation = new OrderRepresentation(order);

			System.out.println(representation.toString());

			OrderRepresentation response = client.createOrder(representation);

			System.out.println( response.getHypermedia().getPaymentLink().get(0).getUri());

			System.out.println(response.getOrder().calculateCost());

		}catch (Exception ex){
			ex.printStackTrace();
		}

	}
}
