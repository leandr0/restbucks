package com.lrgoncalves.product.catalog.service.feeds;

import static com.lrgoncalves.product.catalog.service.repositories.EventStoreBuilder.eventStore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lrgoncalves.product.catalog.service.repositories.EventStore;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Person;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;

public class EventFeedGeneratorTest {
    private static final String FEED_PRODUCER = "Product Catalog";
    private static final int ENTRIES_PER_FEED = 20;

    @Before
    public void clearEventStore() {
        EventStore.current().clear();
    }

    @Test
    public void recentFeedShouldNotContainPrevIfTheNumberOfEntriesIsSmall() throws Exception {
        eventStore().withRandomEvents(12);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/recent"), ENTRIES_PER_FEED);
        Feed feed = generator.getRecentFeed();
        
        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));
        
        Boolean prevLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"prev-archive\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertFalse(prevLinkExists.booleanValue());
    }
    
    @Test
    public void shouldCreateARecentFeedWithSelfAndWellKnownUri() throws Exception {
        eventStore().withRandomEvents(123);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/recent"), ENTRIES_PER_FEED);
        Feed feed = generator.getRecentFeed();

        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));

        Boolean selfLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"self\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertTrue(selfLinkExists.booleanValue());
        
        String selfUri = (String) xPath.compile("/feed/link/@href").evaluate(doc, XPathConstants.STRING);
        assertTrue(selfUri.endsWith("/recent"));
    }
    
    @Test
    public void shouldCreateARecentFeedWithViaLinkWithCanonicalUri() throws Exception  {
        eventStore().withRandomEvents(123);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/recent"), ENTRIES_PER_FEED);
        Feed feed = generator.getRecentFeed();
        
        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));
        
        Boolean viaLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"via\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertTrue(viaLinkExists.booleanValue());
        
        String viaUri = (String) xPath.compile("/feed/link[@rel=\"via\"]/@href").evaluate(doc, XPathConstants.STRING);
        assertTrue(viaUri.endsWith("/120,139"));
    }
    
    
    @Test
    public void shouldCreateARecentFeedWithPrefArchiveLinkWithCanonicalUri() throws Exception  {
        eventStore().withRandomEvents(123);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/recent"), ENTRIES_PER_FEED);
        Feed feed = generator.getRecentFeed();
        
        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));
        
        Boolean prevLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"prev-archive\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertTrue(prevLinkExists.booleanValue());
        
        String prevUri = (String) xPath.compile("/feed/link[@rel=\"prev-archive\"]/@href").evaluate(doc, XPathConstants.STRING);
        assertTrue(prevUri.endsWith("/100,119"));
    }
    
    @Test
    public void shouldGenerateAFeedForGivenEvents() throws Exception {
        eventStore().withRandomEvents(100);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/"), ENTRIES_PER_FEED);
        Feed feed = generator.getArchiveFeed(0);
        assertNotNull(feed.getGenerator().getUrl());
        assertNotNull(feed.getGenerator().getValue());
        assertEquals(FEED_PRODUCER, feed.getGenerator().getValue());
        final List<Person> authors = feed.getAuthors();
        assertEquals(1, authors.size());
        assertNotNull(FEED_PRODUCER, authors.get(0).getName());
        final List<Entry> entries = feed.getEntries();
        assertEquals(ENTRIES_PER_FEED, entries.size());

        for (Entry e : entries) {
            assertNotNull(e.getContents());
            assertTrue(e.getContents().size() > 0);
        }

        try {
            stringify(feed);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void shouldBeAbleToCreateANonFullWorkingFeed() throws Exception {
        int numberOfEventsNotWhollyDivisibleByFeedSize = 210;
        eventStore().withRandomEvents(numberOfEventsNotWhollyDivisibleByFeedSize);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/"), ENTRIES_PER_FEED);
        Feed workingFeed = generator.getWorkingFeed();
        assertEquals(10, workingFeed.getEntries().size());
    }

    @Test
    public void shouldBeAbleToCreateAFullWorkingFeed() throws Exception {
        int numberOfEventsWhollyDivisibleByFeedSize = 220;
        eventStore().withRandomEvents(numberOfEventsWhollyDivisibleByFeedSize);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/"), ENTRIES_PER_FEED);
        Feed workingFeed = generator.getWorkingFeed();
        assertEquals(ENTRIES_PER_FEED, workingFeed.getEntries().size());
    }

    @Test
    public void shouldHaveNextArchiveAndWellKnownUriForTheWorkingFeed() throws Exception {
        eventStore().withRandomEvents(100);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/working"), ENTRIES_PER_FEED);
        Feed feed = generator.getWorkingFeed();

        String xmlString = stringify(feed);

        XPath xPath = createXPath();
        Document doc = createXmlDocument(xmlString);

        int numberOfFeedLinks = 2;
        NodeList links = (NodeList) xPath.compile("/feed/link").evaluate(doc, XPathConstants.NODESET);
        assertEquals(numberOfFeedLinks, links.getLength());

        Boolean altLinkHasType = (Boolean) xPath.compile("/feed/link[@type=\"application/atom+xml\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertTrue(altLinkHasType.booleanValue());

        Boolean altLinkHasHref = (Boolean) xPath.compile("/feed/link[@href=\"http://restbucks.com/product-catalog/notifications/80,99\"]").evaluate(doc,
                XPathConstants.BOOLEAN);
        assertTrue(altLinkHasHref.booleanValue());

        Boolean selfLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"self\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertTrue(selfLinkExists.booleanValue());

        altLinkHasHref = (Boolean) xPath.compile("/feed/link[@href=\"http://restbucks.com/product-catalog/notifications/60,79\"]").evaluate(doc,
                XPathConstants.BOOLEAN);
        assertTrue(altLinkHasHref.booleanValue());

    }

    @Test
    public void shouldNotProduceAPrevLinkForTheOldestFeed() throws Exception {
        eventStore().withRandomEvents(63);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/recent"), ENTRIES_PER_FEED);
        Feed feed = generator.getArchiveFeed(0);

        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));

        Boolean prevArchiveLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"prev-archive\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertFalse(prevArchiveLinkExists.booleanValue());
    }

    @Test
    public void shouldProduceAPrevLinkForAllOtherFeeds() throws Exception {
        eventStore().withRandomEvents(63);
        // All other feeds
        for (int i = ENTRIES_PER_FEED; i < EventStore.current().getNumberOfEvents(); i = i + ENTRIES_PER_FEED) {
            EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/" + i + ","
                    + (i + ENTRIES_PER_FEED - 1)), ENTRIES_PER_FEED);
            Feed feed = generator.getArchiveFeed(i);

            XPath xPath = createXPath();
            Document doc = createXmlDocument(stringify(feed));

            Boolean prevArchiveLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"prev-archive\"]").evaluate(doc, XPathConstants.BOOLEAN);
            assertTrue(prevArchiveLinkExists.booleanValue());
        }
    }

    @Test
    public void shouldNotProduceANextLinkForTheWorkingFeed() throws Exception {
        eventStore().withRandomEvents(63);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/recent"), ENTRIES_PER_FEED);
        Feed feed = generator.getWorkingFeed();

        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));

        Boolean prevArchiveLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"next-archive\"]").evaluate(doc, XPathConstants.BOOLEAN);
        assertFalse(prevArchiveLinkExists.booleanValue());
    }

    @Test
    public void shouldProduceANextLinkForAllOtherFeeds() throws Exception {
        eventStore().withRandomEvents(63);
        // All other feeds
        for (int i = 0; i < EventStore.current().getNumberOfEvents() - ENTRIES_PER_FEED; i = i + ENTRIES_PER_FEED) {
            EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/" + i + "," + (i + ENTRIES_PER_FEED - 1)), ENTRIES_PER_FEED);
            Feed feed = generator.getArchiveFeed(i);

            XPath xPath = createXPath();
            Document doc = createXmlDocument(stringify(feed));

            Boolean prevArchiveLinkExists = (Boolean) xPath.compile("/feed/link[@rel=\"next-archive\"]").evaluate(doc, XPathConstants.BOOLEAN);
            assertTrue(prevArchiveLinkExists.booleanValue());
        }

    }

    @Test
    public void shouldBeAbleToNavigateASetOfFeedsBackwardsAndForwards() throws Exception {
        eventStore().withRandomEvents(105);
        EventFeedGenerator generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/"), ENTRIES_PER_FEED);
        Feed feed = generator.getWorkingFeed();
        assertEquals(5, feed.getEntries().size());

        URI prevLink = getPrevLink(feed);
        while (prevLink.toString() != "") {

            int from = getStartId(prevLink);
            int to = getEndId(prevLink);
            
            generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/" + from + "," + to), ENTRIES_PER_FEED);
            feed = generator.getArchiveFeed(from);
            assertNotNull(feed);
            assertEquals(ENTRIES_PER_FEED, feed.getEntries().size());
            prevLink = getPrevLink(feed);
        }

        URI nextLink = getNextLink(feed);
        
        while (nextLink.toString() != "") {

            int from = getStartId(nextLink);
            int to = getEndId(nextLink);
            
            generator = new EventFeedGenerator(new URI("http://restbucks.com/product-catalog/notifications/" + from + "," + to), ENTRIES_PER_FEED);
            feed = generator.getArchiveFeed(from);
            assertNotNull(feed);
            assertTrue(feed.getEntries().size() > 0);
            nextLink = getNextLink(feed);
        }
    }

    private URI getNextLink(Feed feed) throws Exception {
        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));
        String prevArchiveLink = (String) xPath.compile("/feed/link[@rel=\"next-archive\"]/@href").evaluate(doc, XPathConstants.STRING);
        return new URI(prevArchiveLink);
    }

    private int getEndId(URI prevLink) {
        final String path = prevLink.getPath();
        StringTokenizer st = new StringTokenizer(path);
        int trimLength = st.nextToken(",").length();
        return Integer.valueOf(path.substring(trimLength + 1, path.length()));
    }

    private int getStartId(URI prevLink) {
        final String path = prevLink.getPath();
        String numbers = path.substring(path.lastIndexOf("/") + 1, path.length());
        StringTokenizer st = new StringTokenizer(numbers);
        int lengthToComma = st.nextToken(",").length();
        return Integer.valueOf(numbers.substring(0, lengthToComma));
    }

    private URI getPrevLink(Feed feed) throws Exception {
        XPath xPath = createXPath();
        Document doc = createXmlDocument(stringify(feed));
        String prevArchiveLink = (String) xPath.compile("/feed/link[@rel=\"prev-archive\"]/@href").evaluate(doc, XPathConstants.STRING);
        return new URI(prevArchiveLink);
    }

    private XPath createXPath() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath;
    }

    private Document createXmlDocument(String xmlString) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        Document doc = documentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlString.getBytes()));
        return doc;
    }

    private String stringify(Feed feed) throws IOException, FeedException {
        WireFeedOutput output = new WireFeedOutput();
        final StringWriter stringWriter = new StringWriter();
        output.output(feed, stringWriter);
        return stringWriter.toString();
    }
}