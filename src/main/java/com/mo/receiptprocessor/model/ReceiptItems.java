package com.mo.receiptprocessor.model;
public class ReceiptItems {
    private String shortDescription;
    private String price;

    // Constructor
    public ReceiptItems(String shortDescription, String price) {
        this.shortDescription = shortDescription;
        this.price = price;
    }

    // Getters and Setters
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
