package com.lrgoncalves.product.catalog.service.domain;

import java.net.URI;
import java.util.Random;


public class ProductBuilder {
    public static ProductBuilder product() {
        try {
            return new ProductBuilder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private ProductBuilder(){}

    private String name;
    private String size;
    private double price = -1.0;
    private URI productUri;
    private boolean isUpdate = false;
    private Date createdOn;
    private String createdBy;


    public ProductBuilder withSize(String size) {
        this.size = size;
        return this;
    }
    
    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public ProductBuilder withPrice(double price) {
        this.price = price;
        return this;
    }
    
    public ProductBuilder withProductUri(String string) {
        try {
            productUri = new URI(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public ProductBuilder isNewProduct() {
        isUpdate = false;
        return this;
    }
    
    public ProductBuilder isProductUpdate() {
        isUpdate = true;
        return this;
    }
    
    public ProductBuilder createdAtRandomRecentDate() {
        createdOn = Date.randomDate(new Date(2008, 1, 1), new Date(2009, 8, 7));
        return this;
    }
    
    public ProductBuilder createdBy(String productManager) {
        this.createdBy = productManager;
        return this;
    }

    public Product build() {
        if(name == null) {
            int i = new Random().nextInt(Integer.MAX_VALUE);
            name = "product name " + String.valueOf(i);
        }
        
        if(price < 0.0) {
            price = new Random().nextDouble() * 10.0;
        }
        
        if(productUri == null) {
            try {
                productUri = new URI("http://restbucks.com/products/" + new Random().nextInt(Integer.MAX_VALUE));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        if(createdOn == null) {
            createdOn = new Date();
        }
        
        if(createdBy == null) {
            createdBy = "A Product Manager";
        }
        
        if(size == null) {
            return new Product(name, price, productUri, isUpdate, createdOn.getTimestamp(), createdBy);
        } else {
            return new Product(name, size, price, productUri, isUpdate, createdOn.getTimestamp(), createdBy);
        }
    }
}
