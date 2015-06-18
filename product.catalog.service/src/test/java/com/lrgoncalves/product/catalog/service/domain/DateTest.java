package com.lrgoncalves.product.catalog.service.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DateTest {
    @Test
    public void shouldUseTagUriDateFormat() {
        Date date = new Date(2009, 4, 23);
        String dateString = date.toString();
        assertEquals("2009-04-23", dateString);
    }
    
    @Test
    public void shouldCreateRandomDateBetweenLimits() {
        final int startYear = 2005;
        final int startMonth = 5;
        final int startDay = 6;
        Date d1 = new Date(startYear, startMonth, startDay);
        final int endYear = 2009;
        final int endMonth = 9;
        final int endDay = 10;
        Date d2 = new Date(endYear, endMonth, endDay);
        
        
        for(int i = 0; i < 100; i ++) {
            Date randomDate = Date.randomDate(d1, d2);

            assertTrue(randomDate.getYear() >= startYear);
            assertTrue(randomDate.getMonth() >= startMonth);
            assertTrue(randomDate.getDay() >= startDay);
            
            assertTrue(randomDate.getYear() <= endYear);
            assertTrue(randomDate.getMonth() <= endMonth);
            assertTrue(randomDate.getDay() <= endDay);
        }
    }
}
