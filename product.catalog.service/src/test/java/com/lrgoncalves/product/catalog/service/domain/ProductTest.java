package com.lrgoncalves.product.catalog.service.domain;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.net.URI;

import org.joda.time.DateTime;
import org.junit.Test;

public class ProductTest {
	@Test
	public void productShouldSerialiseFullySpecifiedProductToXml() throws Exception {
		Product product = ProductBuilder.product().createdAtRandomRecentDate().withName("Fairtrade Roma Coffee Beans").withSize("1kg").withPrice(10.0).withProductUri("http://restbucks.com/products/527").build();
		
		String xmlProduct = product.toXmlString();
		
		assertThat(xmlProduct, containsString("<product "));
		assertThat(xmlProduct, containsString("xmlns=\"http://schemas.restbucks.com/product\""));
		assertThat(xmlProduct, containsString("href=\"http://restbucks.com/products/527\""));
		assertThat(xmlProduct, containsString("<name>Fairtrade Roma Coffee Beans</name>"));
		assertThat(xmlProduct, containsString("<size>1kg</size>"));
		assertThat(xmlProduct, containsString("<price>10.0</price>"));
		assertThat(xmlProduct, containsString("</product>"));
	}
	
	@Test
	public void productShouldSerialiseMinimallySpecifiedProductToXml() throws Exception {
		Product product = new Product("Cute Fluffy Mascot", 5.0, new URI("http://restbucks.com/products/527"), false, new DateTime(), "Some Product Manager");
		
		String xmlProduct = product.toXmlString();
		
		assertThat(xmlProduct, containsString("<product "));
        assertThat(xmlProduct, containsString("xmlns=\"http://schemas.restbucks.com/product\""));
        assertThat(xmlProduct, containsString("href=\"http://restbucks.com/products/527\""));
		assertThat(xmlProduct, containsString("<name>Cute Fluffy Mascot</name>"));
		assertThat(xmlProduct, containsString("<price>5.0</price>"));
		assertThat(xmlProduct, containsString("</product>"));
		assertThat(xmlProduct, not(containsString("<size>")));
	}
}