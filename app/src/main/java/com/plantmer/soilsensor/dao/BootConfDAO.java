package com.plantmer.soilsensor.dao;

public class BootConfDAO {
    private UserDTO user;
    private String message;
    private String token;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "BootConfDAO{" +
                "user=" + user +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
