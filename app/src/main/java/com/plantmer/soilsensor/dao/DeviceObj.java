package com.plantmer.soilsensor.dao;


public class DeviceObj {

    private String deviceId;
    private String name;
    private String devType;

    public DeviceObj( ) {
    }

    public DeviceObj(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
    }


    public DeviceObj(String deviceId, String name, String devType) {
        this.deviceId = deviceId;
        this.name = name;
        this.devType = devType;
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

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    @Override
    public String toString() {
        return "DeviceObj{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", devEui='" + devType + '\'' +
                '}';
    }
}
