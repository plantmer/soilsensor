package com.plantmer.soilsensor.util;

/**
 * Created by edgars.martinovs on 2018-06-07.
 */

public class DataObj {

    private long dateTime;
    private float dp;
    private float ec;
    private float temp;
    private float vwc;

    public DataObj(long dateTime) {
        this.dateTime = dateTime;
    }

    public DataObj(long dateTime, float dp, float ec, float temp, float vwc) {
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
