package com.lrgoncalves.product.catalog.service.domain;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PromotionBuilder {
    public static PromotionBuilder promotion() {
        try {
            return new PromotionBuilder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Date effectiveFrom;
    private List<Link> products;
    private List<Link> regions;
    private URI uri;
    private boolean promotionUpdate;
    private Date createdOn;
    private String createdBy;

    private PromotionBuilder() {}

    public PromotionBuilder withEffectiveFromDate(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
        return this;
    }

    public PromotionBuilder withProductLink(Link productLink) {
        if(products == null) {
            products = new ArrayList<Link>();
        }
        products.add(productLink);
        return this;
    }
    
    public PromotionBuilder withRegionLink(Link regionLink) {
        if(regions == null) {
            regions = new ArrayList<Link>();
        }
        regions.add(regionLink);
        return this;
    }

    public PromotionBuilder withRandomRegions() throws Exception {
       regions = new ArrayList<Link>();
        
        for(int i = 0; i < (System.currentTimeMillis() % 5) + 1; i++) { // at least one, at most 5
            regions.add(new Link(new URI("http://restbucks.com/regions/" + i), "application/vnd.restbucks+xml"));
        }
        
        return this;
    }

    public PromotionBuilder withRandomProducts() throws Exception {
        products = new ArrayList<Link>();
        
        for(int i = 0; i < (System.currentTimeMillis() % 5) + 1; i++) { // at least one, at most 5
            products.add(new Link(new URI("http://restbucks.com/products/" + i), "application/vnd.restbucks+xml"));
        }
        
        return this;
    }
    
    public PromotionBuilder isPromotionUpdate() {
        promotionUpdate = true;
        return this;
    }
    
    public PromotionBuilder isNewPromotion() {
        promotionUpdate = false;
        return this;
    }
    
    public PromotionBuilder createdAtRandomRecentDate() {
        createdOn = Date.randomDate(new Date(2008, 1, 1), new Date(2009, 8, 7));
        return this;
    }
    
    public PromotionBuilder createdBy(String promotionManager) {
        this.createdBy = promotionManager;
        return this;
    }
    
    public Promotion build() {
        if (effectiveFrom == null) {
            effectiveFrom = new Date();
        }

        try {
            if (products == null) {
                products = new ArrayList<Link>();
                products.add(new Link(new URI("http://restbucks.com/products/" + new Random().nextInt(Integer.MAX_VALUE)), "application/vnd.restbucks+xml"));
            }

            if (regions == null) {
                regions = new ArrayList<Link>();
                regions.add(new Link(new URI("http://restbucks.com/regions/" + new Random().nextInt(Integer.MAX_VALUE)), "application/vnd.restbucks+xml"));
            }
            
            if(uri == null) {
                uri = new URI("http://restbucks.com/promotions/" + new Random().nextInt(Integer.MAX_VALUE));
            }
            
            if(createdOn == null) {
                createdOn = new Date();
            }
            
            if(createdBy == null) {
                createdBy = "Promotion Manager";
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return new Promotion(effectiveFrom, products, regions, uri, promotionUpdate, createdOn.getTimestamp(), createdBy);
    }
}
