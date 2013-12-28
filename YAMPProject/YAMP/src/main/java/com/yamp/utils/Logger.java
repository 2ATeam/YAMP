package com.yamp.utils;

import android.support.v7.appcompat.R;
import android.util.Log;

import com.yamp.core.AudioManager;
import com.yamp.core.YAMPApplication;
import com.yamp.events.NewTrackLoadedListener;
import com.yamp.events.PlayingCompletedListener;
import com.yamp.events.PlayingStartedListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.library.AudioFile;
import com.yamp.sound.SoundController;

/**
 * Created by AdYa on 07.12.13.
 */
public class Logger {
    // Provides organized logging system.

    private static Logger logger = new Logger();
    public static Logger getInstance(){
        return logger;
    }



    private boolean isEnabled = true;
    public static void enable() {
        logger.isEnabled = true;
    }
    public static void disable() {
        logger.isEnabled = false;
    }

    private void sendMessage(String tag, String msg){
        if (isEnabled)
            Log.i(tag, msg);
    }

    private Logger(){
        YAMPApplication.setOnSoundControllerBoundedListener(new SoundControllerBoundedListener() {
            @Override
            public void onSoundControllerBounded(SoundController controller) {
                sendMessage("YAMP Application", "Sound Controller has been bounded");
                setSoundControllerDependedClasses();
            }
        });
    }
    private void setSoundControllerDependedClasses(){
        AudioManager.getInstance().setOnNewTrackLoadedListener(new NewTrackLoadedListener() {
            @Override
            public void onNewTrackLoaded(AudioFile track) {
                sendMessage("Audio Manager", "Track \"" + track.getName() + "\" was loaded.");
            }
        });
        YAMPApplication.getSoundController().setOnPlayingCompletedListener(new PlayingCompletedListener() {
            @Override
            public void onPlayingCompleted() {
                sendMessage("Sound Controller", "Track has been completed.");
            }
        });

        YAMPApplication.getSoundController().setOnPlayingStartedListener(new PlayingStartedListener() {
            @Override
            public void onPlayingStarted() {
                sendMessage("Sound Controller", "Track has been started.");
            }
        });
    }

    public static void setGestureAdapter(GestureAdapter gestureAdapter){
        gestureAdapter.setOnFlingListener(new GestureAdapter.FlingListener() {
            @Override
            public void onUpFling() {
                logger.sendMessage("Gesture Adapter", "Up fling");
            }

            @Override
            public void onDownFling() {
                logger.sendMessage("Gesture Adapter", "Down fling");
            }

            @Override
            public void onLeftFling() {
                logger.sendMessage("Gesture Adapter", "Left fling");
            }

            @Override
            public void onRightFling() {
                logger.sendMessage("Gesture Adapter", "Right fling");
            }
        });

        gestureAdapter.setOnTapListener(new GestureAdapter.TapListener() {
            @Override
            public void onDoubleTap() {
                logger.sendMessage("Gesture Adapter", "Double tap");
            }

            @Override
            public void onLongTap() {
                logger.sendMessage("Gesture Adapter", "Long tap");
            }

            @Override
            public void onTapStarted() {
                logger.sendMessage("Gesture Adapter", "Tap started");
            }
        });
    }
}
