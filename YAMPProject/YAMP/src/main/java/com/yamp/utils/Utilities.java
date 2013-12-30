package com.yamp.utils;

/**
 * Created by AdYa on 04.12.13.
 */
public class Utilities {
    /**
     * Clamps specified value between min and max
     * @param min Lower bound inclusive
     * @param max Upper bound inclusive
     * @param value Clamping value
     * @return Clamped value
     */
    public static int clamp(int min, int max, int value){
        return (value < min ? min : (value > max ? max : value));
    }

    /**
     * Checks specified value to be between min and max
     * @param min Lower bound inclusive
     * @param max Upper bound inclusive
     * @param value Target value
     * @return true if value is in range, otherwise - false
     */
    public static boolean isWithin(int min, int max, int value){
        return (value >= min && value <= max);
    }

    /**
     * Formats time given in milliseconds to "mm:ss" format
     * @param msec Time in milliseconds
     * @return Formatted String
     */
    public static String formatTime(int msec){
        int m = msec / 60000;
        int s = msec / 1000 - m * 60;
        return (m < 10 ? "0" : "") + String.valueOf(m) + ":" + (s < 10 ? "0" : "") + String.valueOf(s);
    }

    /**
     *
     * @param min Min value inclusive
     * @param max Max value exclusive
     * @return Random int in range [min; max)
     */
    public static int randomInt(int min, int max){
        return (int)(Math.random() * max) + min;
    }
}
