/**
 * 
 */
package com.lrgoncalves.restbucks.sales;

import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.lrgoncalves.restbucks.domain.Drink;
import com.lrgoncalves.restbucks.domain.Item;
import com.lrgoncalves.restbucks.domain.Milk;
import com.lrgoncalves.restbucks.domain.Order;
import com.lrgoncalves.restbucks.domain.Size;
import com.lrgoncalves.restbucks.representations.OrderRepresentation;
import com.lrgoncalves.restbucks.service.order.client.OrderServiceClient;

/**
 * @author lrgoncalves
 *
 */
@ManagedBean(name = "createOrderMB")
@SessionScoped
public class CreateOrderManagerBean {

	@Inject
	private OrderServiceClient orderServiceClient;

	/**
	 * 
	 */
	public CreateOrderManagerBean() {
		// TODO Auto-generated constructor stub
	}

	private String selectedDrink;

	private String selectedSize;

	private String selectedMilk;

	private List<Item> items = new LinkedList<Item>();

	public String addItem(){

		Size 	size 	= Size.valueOf(selectedSize);
		Drink 	drink 	= Drink.valueOf(selectedDrink);
		Milk 	milk	= Milk.valueOf(selectedMilk);

		items.add(new Item(size, milk, drink));

		resetSelects();

		return null;
	}

	public String removeItem(final Item item){

		items.remove(item);

		addMessage("Success","Item removed");

		return null;
	}

	public String createOrder(){

		Order order = new Order();

		for(Item item : items){
			order.addItem(item);
		}

		try{

			OrderRepresentation orderRepresentation =
					orderServiceClient.createOrder(new OrderRepresentation(order));

			addMessage("Success", "Order Created : "+orderRepresentation.getOrder().calculateCost() );

		}catch(Exception ex){
			
			addMessage("Error", ex.getMessage());
			
		}finally {
			
			clearItems();
			resetSelects();

		}


		return null;
	}

	private void clearItems(){

		items.clear();
	}

	private void resetSelects(){

		this.selectedDrink	= null;
		this.selectedMilk 	= null;
		this.selectedSize	= null;
	}

	//addMessage("Success", "Data saved");

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}



	public String getSelectedDrink() {
		return selectedDrink;
	}

	public void setSelectedDrink(String selectedDrink) {
		this.selectedDrink = selectedDrink;
	}

	public String getSelectedSize() {
		return selectedSize;
	}

	public void setSelectedSize(String selectedSize) {
		this.selectedSize = selectedSize;
	}

	public String getSelectedMilk() {
		return selectedMilk;
	}

	public void setSelectedMilk(String selectedMilk) {
		this.selectedMilk = selectedMilk;
	}

	public void addMessage(String summary, String detail) {
		/*FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);*/

		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(summary,  detail) );

	}
}