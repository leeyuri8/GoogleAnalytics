package com.yrabdelrhmn.googleanalytics;

import java.io.Serializable;

public class Product implements Serializable {
    String productName;
    String productImage;
    String productDetails;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public Product(String productName, String productImage, String productDetails) {
        this.productName = productName;
        this.productImage = productImage;
        this.productDetails = productDetails;
    }
    public Product(){

    }

}
