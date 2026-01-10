
package com.example.gameapp.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GamesResponse {

    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Game> data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Game> getData() {
        return data;
    }

    public static class Game {
        @SerializedName("name")
        private String name;

        @SerializedName("image")
        private String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}