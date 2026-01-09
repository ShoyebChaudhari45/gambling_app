package com.example.gameapp.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TapsResponse {

    @SerializedName("status_code")
    public int statusCode;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<GameTap> data;

    public static class GameTap {
        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String name;

        @SerializedName("times")
        public List<Tap> times;
    }

    public static class Tap {
        @SerializedName("id")
        public int id;

        @SerializedName("type")
        public String type; // "open" or "close"

        @SerializedName("end_time")
        public String endTime;

        @SerializedName("status")
        public String status; // "closed" or "open"
    }
}