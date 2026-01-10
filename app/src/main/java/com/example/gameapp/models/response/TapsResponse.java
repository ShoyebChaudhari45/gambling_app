package com.example.gameapp.models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class TapsResponse {

    @SerializedName("data")
    private List<GameData> data;

    public List<GameData> getData() {
        return data;
    }

    // ================= GAME =================
    public static class GameData {

        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name; // ✅ GAME NAME

        @SerializedName("times")
        private List<Tap> times;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Tap> getTimes() {
            return times;
        }
    }

    // ================= TAP =================
    public static class Tap {

        @SerializedName("id")
        private int id;

        @SerializedName("type")
        private String type;

        @SerializedName("end_time")
        private String endTime;

        @SerializedName("status")
        private String status;

        // runtime only (not from API)
        private String gameName;

        public int getId() { return id; }
        public String getType() { return type; }
        public String getEndTime() { return endTime; }
        public String getStatus() { return status; }

        public String getGameName() { return gameName; }
        public void setGameName(String gameName) { this.gameName = gameName; }
    }
}
