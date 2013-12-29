package com.yamp.utils;

/**
 * Created by AdYa on 04.12.13.
 */
public class Utilities {
    public static int clamp(int min, int max, int value){
        return (value < min ? min : (value > max ? max : value));
    }

    public static String formatTime(int msec){
        int m = msec / 60000;
        int s = msec / 1000 - m * 60;
        return (m < 10 ? "0" : "") + String.valueOf(m) + ":" + (s < 10 ? "0" : "") + String.valueOf(s);
    }
}
