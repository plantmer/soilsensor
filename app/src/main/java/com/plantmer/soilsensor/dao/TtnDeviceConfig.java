package com.plantmer.soilsensor.dao;

public class TtnDeviceConfig {
    private String type="otaa";
    private String appEui;
    private String devEui;
    private String appKey;

    public TtnDeviceConfig(String appEui, String devEui, String appKey) {
        this.appEui = appEui;
        this.devEui = devEui;
        this.appKey = appKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppEui() {
        return appEui;
    }

    public void setAppEui(String appEui) {
        this.appEui = appEui;
    }

    public String getDevEui() {
        return devEui;
    }

    public void setDevEui(String devEui) {
        this.devEui = devEui;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
