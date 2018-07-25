package com.plantmer.soilsensor.dao;


public class DeviceObj {

    private String deviceId;
    private String name;
    private String devEui;
    private String appEui;
    private String appKey;

    public DeviceObj( ) {
    }

    public DeviceObj(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
    }


    public DeviceObj(String deviceId, String name, String devEui, String appEui, String appKey) {
        this.deviceId = deviceId;
        this.name = name;
        this.devEui = devEui;
        this.appEui = appEui;
        this.appKey = appKey;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevEui() {
        return devEui;
    }

    public void setDevEui(String devEui) {
        this.devEui = devEui;
    }

    public String getAppEui() {
        return appEui;
    }

    public void setAppEui(String appEui) {
        this.appEui = appEui;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return "DeviceObj{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", devEui='" + devEui + '\'' +
                ", appEui='" + appEui + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}
