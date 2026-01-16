package com.example.gameapp.models.response;

import com.google.gson.annotations.SerializedName;

public class BidItem {

    @SerializedName("type")
    private String type;

    @SerializedName("input_value")
    private String inputValue;

    @SerializedName("price")
    private int price;

    @SerializedName("created_on")
    private String createdOn;

    // Constructors
    public BidItem() {}

    public BidItem(String type, String inputValue, int price, String createdOn) {
        this.type = type;
        this.inputValue = inputValue;
        this.price = price;
        this.createdOn = createdOn;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getInputValue() {
        return inputValue;
    }

    public int getPrice() {
        return price;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
