package com.yamp.utils;

/**
 * Created by AdYa on 04.12.13.
 */
public class Utilities {
    public static int clamp(int min, int max, int value){
        return (value < min ? min : (value > max ? max : value));
    }
}
