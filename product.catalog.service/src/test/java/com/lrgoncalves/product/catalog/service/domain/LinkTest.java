package com.lrgoncalves.product.catalog.service.domain;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;
import java.net.URI;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;


public class LinkTest {
	@Test
	public void fullLinksShoudlSerialiseAsAtomLinks() throws Exception {
		Link link = new Link(new URI("http://example.org/foo"), "alternate", "application/xml");
		
		String xmlString = serialiseToXml(link);
		
		assertThat(xmlString, containsString("type=\"application/xml\""));
		assertThat(xmlString, containsString("rel=\"alternate\""));
		assertThat(xmlString, containsString("href=\"http://example.org/foo\""));
	}
	
	@Test
	public void linksWithoutAltAttributedShoudlSerialiseAsAtomLinks() throws Exception {
		Link link = new Link(new URI("http://example.org/foo"), "application/xml");
		
		String xmlString = serialiseToXml(link);
		
		assertThat(xmlString, containsString("type=\"application/xml\""));
		assertThat(xmlString, not(containsString("rel")));
		assertThat(xmlString, containsString("href=\"http://example.org/foo\""));
	}
	
	private String serialiseToXml(Link link) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Link.class);
        Marshaller marshaller = context.createMarshaller();

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(link, stringWriter);

        return stringWriter.toString();
	}
}
