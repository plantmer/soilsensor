package com.plantmer.soilsensor.dao;

import java.util.Map;

public class DataSourceDTO {
    private String id;//:
    private String name;//:
    private String type;//:
    private boolean online;
    private String descr;
    private Map<Integer,String> cols;
    private long expires;
    private double loc_lat;
    private double loc_long;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Map<Integer, String> getCols() {
        return cols;
    }

    public void setCols(Map<Integer, String> cols) {
        this.cols = cols;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public double getLoc_lat() {
        return loc_lat;
    }

    public void setLoc_lat(double loc_lat) {
        this.loc_lat = loc_lat;
    }

    public double getLoc_long() {
        return loc_long;
    }

    public void setLoc_long(double loc_long) {
        this.loc_long = loc_long;
    }

    @Override
    public String toString() {
        return "DataSourceDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", online=" + online +
                ", descr='" + descr + '\'' +
                ", cols=" + cols +
                ", expires=" + expires +
                ", loc_lat=" + loc_lat +
                ", loc_long=" + loc_long +
                '}';
    }
}
