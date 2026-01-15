package com.example.gameapp.models.request;



public class LotteryRateRequest {

    private int time_id;
    private String type;
    private String digit;
    private int price;

    public LotteryRateRequest(int time_id, String type, String digit, int price) {
        this.time_id = time_id;
        this.type = type;
        this.digit = digit;
        this.price = price;
    }
}
