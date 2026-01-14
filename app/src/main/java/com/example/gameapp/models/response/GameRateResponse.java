package com.example.gameapp.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GameRateResponse {

    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<GameRateItem> data;

    public List<GameRateItem> getData() {
        return data;
    }
}
