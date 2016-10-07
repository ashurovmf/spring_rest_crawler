package com.gft.backend.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miav on 2016-09-29.
 */
public class EBayItem {
    private String itemId;
    private String title;
    private String categoryId;
    private String itemURL;
    private String currency;
    private String currentPrice;
    private List<String> paymentMethods = new ArrayList<>(4);

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethod) {
        this.paymentMethods.add(paymentMethod);
    }
}
