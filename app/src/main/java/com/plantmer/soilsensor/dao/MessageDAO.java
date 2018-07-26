package com.plantmer.soilsensor.dao;

public class MessageDAO {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageDAO{" +
                "message='" + message + '\'' +
                '}';
    }
}
