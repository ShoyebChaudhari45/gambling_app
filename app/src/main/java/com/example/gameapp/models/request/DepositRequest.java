package com.example.gameapp.models.request;

public class DepositRequest {

    private int price;

    public DepositRequest(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
