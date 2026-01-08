package com.example.gameapp.models.response;



public class CommonResponse {
    private boolean success;
    private String message;
    private Object data;

    // Added Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}
