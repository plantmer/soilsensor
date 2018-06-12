package com.plantmer.soilsensor.util;

import android.app.AlarmManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;
import java.util.Random;

public class Utils {

    public static String randomHex(int size){
        StringBuilder ret = new StringBuilder("");
        Random rnd = new Random();
        for(int i=0;i<size;i++){
            ret.append(Integer.toHexString(rnd.nextInt(255)));
        }
        return ret.toString().toUpperCase();
    }

    public static String setLastUpdateTime(Context context, long lastUpdate) {
        Date lastUpdateTime = new Date(lastUpdate);
        return DateFormat.getTimeFormat(context).format(lastUpdateTime);
    }

    public static String unixTimeToFormatTime(Context context, long unixTime) {
        long unixTimeToMillis = unixTime * 1000;
        return DateFormat.getTimeFormat(context).format(unixTimeToMillis);
    }

    public static void copyToClipboard(Context context, String string) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(string, string);
        clipboardManager.setPrimaryClip(clipData);
    }
}
