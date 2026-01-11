package com.example.gameapp.models.response;

public class DepositResponse {

    private int status_code;
    private String message;
    private Data data;

    public int getStatus_code() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private int employee_id;
        private String type;
        private int amount;

        public int getEmployee_id() {
            return employee_id;
        }

        public String getType() {
            return type;
        }

        public int getAmount() {
            return amount;
        }
    }
}
