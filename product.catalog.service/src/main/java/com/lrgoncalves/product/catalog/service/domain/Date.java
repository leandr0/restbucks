package com.lrgoncalves.product.catalog.service.domain;

import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Date implements Comparable<Date> {
    private DateTime date;
    public Date(int year, int month, int day) {
        date = new DateTime().withYear(year).withMonthOfYear(month).withDayOfMonth(day);
    }
    
    public Date(DateTime timestamp) {
        date = timestamp;
    }
    
    public Date() {
        date = new DateTime();
    }
    
    public DateTime getTimestamp() {
        return date;
     }

    public int getYear() {
        return date.getYear();
    }
    
    public int getMonth() {
        return date.getMonthOfYear();
    }
    
    public int getDay() {
        return date.getDayOfMonth();
    }
    
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        return formatter.print(date);
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof Date) {
            Date d = (Date)obj;
            return getYear() == d.getYear() && getMonth() == d.getMonth() && getDay() == d.getDay();
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return getYear() * 100 + getMonth() * 10 + getDay();
    }

    public int compareTo(Date date) {
        if(this.hashCode() > date.hashCode()) {
            return 1;
        } else if (this.hashCode() == date.hashCode()) {
            return 0;
        } else {
            return -1;
        }
    }

    public static Date randomDate(Date earliest, Date latest) {
        return new Date(pickANumberBetweenInclusive(earliest.getYear(), latest.getYear()),
                pickANumberBetweenInclusive(earliest.getMonth(), latest.getMonth()),
                pickANumberBetweenInclusive(earliest.getDay(), latest.getDay()));
    }
    
    private static int pickANumberBetweenInclusive(int i, int j) {
        int difference = 1 + j - i; // +1 to include j
        
        return (new Random().nextInt(Integer.MAX_VALUE) % difference) + i;
    }
}
