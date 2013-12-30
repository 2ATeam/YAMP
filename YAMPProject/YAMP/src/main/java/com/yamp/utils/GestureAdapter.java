package com.yamp.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by AdYa on 28.12.13.
 */
public class GestureAdapter implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private static final float GESTURE_SENSIVITY = 30;

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        notifyDoubleTap();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        notifyTapStarted();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        notifyLongTap();
    }

    @Override
    public boolean onFling(MotionEvent downMotionEvent, MotionEvent moveMotionEvent, float velocityX, float velocityY) {

        float startX = downMotionEvent.getX();
        float startY = downMotionEvent.getY();
        float endX = moveMotionEvent.getX();
        float endY = moveMotionEvent.getY();


        if (Math.abs(velocityX) - Math.abs(velocityY) < 0){
            if (Math.abs(startY - endY) < GESTURE_SENSIVITY)  return true;
            if (endY> startY)
                notifyDownFling();
            else
                notifyUpFling();
        }
        else{
            if (Math.abs(startX - endX) < GESTURE_SENSIVITY)  return true;
            if (endX > startX)
                notifyRightFling();
            else
                notifyLeftFling();
        }

        return true;
    }


/// Listeners go below...be aware...

    public static interface FlingListener{
        public void onUpFling();
        public void onDownFling();
        public void onLeftFling();
        public void onRightFling();
    }

    private ArrayList<FlingListener> flingListeners = new ArrayList<>();
    public void setOnFlingListener(FlingListener listener){
        flingListeners.add(listener);
    }
    private void notifyUpFling(){
        for(FlingListener listener : flingListeners){
            listener.onUpFling();
        }
    }
    private void notifyDownFling(){
        for(FlingListener listener : flingListeners){
            listener.onDownFling();
        }
    }
    private void notifyLeftFling(){
        for(FlingListener listener : flingListeners){
            listener.onLeftFling();
        }
    }
    private void notifyRightFling(){
        for(FlingListener listener : flingListeners){
            listener.onRightFling();
        }
    }

    public static interface TapListener{
        public void onDoubleTap();
        public void onLongTap();
        public void onTapStarted();
    }

    private ArrayList<TapListener> tapListeners = new ArrayList<>();
    public void setOnTapListener(TapListener listener){
        tapListeners.add(listener);
    }
    private void notifyLongTap(){
        for (TapListener listener : tapListeners) {
            listener.onLongTap();
        }
    }
    private void notifyDoubleTap(){
        for (TapListener listener : tapListeners) {
            listener.onDoubleTap();
        }
    }
    private void notifyTapStarted(){
        for (TapListener listener : tapListeners) {
            listener.onTapStarted();
        }
    }
}
