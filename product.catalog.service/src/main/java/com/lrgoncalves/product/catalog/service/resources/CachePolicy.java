package com.lrgoncalves.product.catalog.service.resources;

public class CachePolicy {

    private static final int ONE_HOUR = 60*60;
    private static final int ONE_MONTH = ONE_HOUR * 24 * 30;

    public static int getWorkingFeedLifetime() {
        return ONE_HOUR;
    }
    
    public static int getArchiveFeedLifetime() {
        return ONE_MONTH;
    }

	public static int getRecentFeedLifetime() {
		// Same policy as working feed
		return getWorkingFeedLifetime();
	}

}
