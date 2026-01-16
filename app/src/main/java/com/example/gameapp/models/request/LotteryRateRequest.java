package com.example.gameapp.models.request;



public class LotteryRateRequest {

    private final int time_id;
    private final String type;
    private final String digit;
    private final int price;

    public LotteryRateRequest(int time_id, String type, String digit, int price) {
        this.time_id = time_id;
        this.type = type;
        this.digit = digit;
        this.price = price;
    }
}
