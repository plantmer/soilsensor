package com.plantmer.soilsensor.dao;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(indices = {@Index("deviceId")})
public class DataObj {

    @PrimaryKey
    private long dateTime;
    private String deviceId;
    @ColumnInfo(name = "dp")
    private float dp;
    @ColumnInfo(name = "ec")
    private float ec;
    @ColumnInfo(name = "temp")
    private float temp;
    @ColumnInfo(name = "vwc")
    private float vwc;


    public DataObj() {}

    @Ignore
    public DataObj(long dateTime) {
        this.dateTime = dateTime;
    }

    @Ignore
    public DataObj(String devId, long dateTime, float dp, float ec, float temp, float vwc) {
        this.deviceId = devId;
        this.dateTime = dateTime;
        this.dp = dp;
        this.ec = ec;
        this.temp = temp;
        this.vwc = vwc;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public float getDp() {
        return dp;
    }

    public void setDp(float dp) {
        this.dp = dp;
    }

    public float getEc() {
        return ec;
    }

    public void setEc(float ec) {
        this.ec = ec;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getVwc() {
        return vwc;
    }

    public void setVwc(float vwc) {
        this.vwc = vwc;
    }
}
