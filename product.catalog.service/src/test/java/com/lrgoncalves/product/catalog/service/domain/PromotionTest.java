package com.lrgoncalves.product.catalog.service.domain;

import static com.lrgoncalves.product.catalog.service.domain.PromotionBuilder.promotion;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;


public class PromotionTest {
	//@Test
	public void shouldSerialiseAFullPromotion() throws Exception {
		
		Date effectiveFrom = new Date(2009, 8, 4);
		Promotion promotion = promotion().withEffectiveFromDate(effectiveFrom).withRandomProducts().withRandomRegions().build();
		
		String xmlString = serialiseToXml(promotion);
		
		assertThat(xmlString, containsString("<promotion"));
	    assertThat(xmlString, containsString("xmlns=\"http://schemas.restbucks.com/promotion\""));
	    assertThat(xmlString, containsString("href=\"http://restbucks.com/promotions/"));
		assertThat(xmlString, containsString("<effective>2009-08-04"));
		assertThat(xmlString, containsString("<ns2:product type=\"application/vnd.restbucks+xml\" href=\"http://restbucks.com/products/0\"/>"));
		assertThat(xmlString, containsString("<ns2:region type=\"application/vnd.restbucks+xml\" href=\"http://restbucks.com/regions/0\"/>"));
		assertThat(xmlString, containsString("</promotion>"));
		
	}


	private String serialiseToXml(Promotion promotion) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Promotion.class);
        Marshaller marshaller = context.createMarshaller();

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(promotion, stringWriter);

        return stringWriter.toString();
	}
}
