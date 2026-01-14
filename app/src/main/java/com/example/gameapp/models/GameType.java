package com.example.gameapp.models;

import com.google.gson.annotations.SerializedName;

public class GameType {

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
