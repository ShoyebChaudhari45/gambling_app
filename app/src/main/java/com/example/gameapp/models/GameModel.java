package com.example.gameapp.models;

import com.example.gameapp.models.response.TapsResponse;
import java.util.List;




public class GameModel {

    private String name;
    private String image;
    private List<TapsResponse.Tap> taps;

    public GameModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<TapsResponse.Tap> getTaps() {
        return taps;
    }

    public void setTaps(List<TapsResponse.Tap> taps) {
        this.taps = taps;
    }
}
