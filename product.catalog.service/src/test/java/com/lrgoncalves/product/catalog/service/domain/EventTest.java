package com.lrgoncalves.product.catalog.service.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.junit.Test;


public class EventTest {

    private static URI defaultUri; 
    
    static {
        try {
            defaultUri = new URI("http://restbucks.com/product-catalog");
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
    
    @Test
    public void eachUpdateShouldHaveAUniqueId() {
        TestableEvent testableUpdate1 = new TestableEvent(defaultUri);
        TestableEvent testableUpdate2 = new TestableEvent(defaultUri);
        assertFalse(testableUpdate1.getId() == testableUpdate2.getId());
    }
    
    @Test
    public void eventIdShouldBeTagUriFormat() {
        TestableEvent testableUpdate = new TestableEvent(defaultUri);
        String id = testableUpdate.getTagUri();
        
        StringTokenizer tokenizer = new StringTokenizer(id, ":");
        int numberOfColonsInTagUri = 2;
        assertEquals(numberOfColonsInTagUri, tokenizer.countTokens() -1);
        
        tokenizer = new StringTokenizer(id, ",");
        int numberOfCommasInTagUri = 1;
        assertEquals(numberOfCommasInTagUri, tokenizer.countTokens() - 1);
    }
    
    @Test
    public void eventsShouldHaveATimestamp() throws Exception {
        Event event = new TestableEvent(defaultUri);
        assertTrue(event.getUpdated().isBeforeNow() || event.getUpdated().isEqualNow());
    }
    
    private static class TestableEvent extends Event {

        protected TestableEvent(URI uri) {
            super(uri, false, new DateTime(), "A product manager");
        }

        public String getEventType() {
            return "testableEvent";
        }

        public String toXmlString() {
            return "<test/>";
        } }
}

/*
<entry>
<id>tag:restbucks.com,2009-08-05:85635</id>
<title type="text">product created</title>
<updated>2009-07-05T10:25:00Z</updated>
<link rel="self" href="http://restbucks.com/product-catalog/notifications/95506d98-aae9-4d34-a8f4-1ff30bece80c"/>
*<link ref="related" href="http://restbucks.com/products/527"/>*
<category scheme="http://restbucks.com/product-catalog/categories/type"
term="product"/>
<category scheme="
http://restbucks.com/product-catalog/categories/status" term="new"/>
<content type="application/vnd.restbucks.product+xml">
  <product xmlns="http://restbucks.com/products" href="
http://restbucks.com/products/527">
    <name>Fairtrade Roma Coffee Beans</name>
    <size>1kg</size>
    <price>10</price>
  </product>
</content>
</entry>

*/