package com.yamp.utils;

import android.util.Log;

import com.yamp.core.AudioManager;
import com.yamp.core.YAMPApplication;
import com.yamp.events.PlaybackListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.events.TrackLoadedListener;
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
        AudioManager.getInstance().setTrackLoadedListener(new TrackLoadedListener() {
            @Override
            public void onNewTrackLoaded(AudioFile track) {
                sendMessage("Audio Manager", "Track \"" + track.getName() + "\" was loaded.");
            }

            @Override
            public void onNextTrackLoaded(AudioFile nextTrack) {
                sendMessage("Audio Manager", "Next track \"" + nextTrack.getName() + "\" was loaded.");
            }

            @Override
            public void onPrevTrackLoaded(AudioFile prevTrack) {
                sendMessage("Audio Manager", "Prev track \"" + prevTrack.getName() + "\" was loaded.");
            }
        });
        YAMPApplication.getSoundController().setPlaybackListener(new PlaybackListener() {
            @Override
            public void onPlayingStarted(boolean causedByUser) {
                sendMessage("Sound Controller", "Track has been started.");
            }

            @Override
            public void onPlayingCompleted(boolean causedByUser) {
                sendMessage("Sound Controller", "Track has been completed.");
            }

            @Override
            public void onPlayingPaused(int currentProgress) {
                sendMessage("Sound Controller", "Track has been paused (at " + Utilities.formatTime(currentProgress) + ").");
            }

            @Override
            public void onPlayingResumed(int currentProgress) {
                sendMessage("Sound Controller", "Track has been resumed (at " + Utilities.formatTime(currentProgress) + ").");
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
