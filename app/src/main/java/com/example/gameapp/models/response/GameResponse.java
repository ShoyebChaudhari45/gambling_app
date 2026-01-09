package com.example.gameapp.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GameResponse {

    @SerializedName("status_code")
    public int statusCode;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<Game> data;

    public static class Game {
        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String name;

        @SerializedName("result")
        public String result;

        @SerializedName("time")
        public String time;

        @SerializedName("image")
        public String image;

        // Constructor for logging
        @Override
        public String toString() {
            return "Game{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", result='" + result + '\'' +
                    ", time='" + time + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }
    }
}