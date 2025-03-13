package com.mo.receiptprocessor.model;
import java.util.List;

public class Receipt {
    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    private List<ReceiptItems> items;
    private String total;


    // Getters and Setters
    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public List<ReceiptItems> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItems> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

