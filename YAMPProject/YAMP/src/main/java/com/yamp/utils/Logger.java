package com.yamp.utils;

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

    public Logger(){
        YAMPApplication.setOnSoundControllerBoundedListener(new SoundControllerBoundedListener() {
            @Override
            public void onSoundControllerBounded(SoundController controller) {
                Log.i("YAMP Application", "Sound Controller has been bounded");
                setSoundControllerDependedClasses();
            }
        });
    }

    private void setSoundControllerDependedClasses(){
        AudioManager.getInstance().setOnNewTrackLoadedListener(new NewTrackLoadedListener() {
            @Override
            public void onNewTrackLoaded(AudioFile track) {
                Log.i("Audio Manager", "Track \"" + track.getName() + "\" was loaded.");
            }
        });
        YAMPApplication.getSoundController().setOnPlayingCompletedListener(new PlayingCompletedListener() {
            @Override
            public void onPlayingCompleted() {
                Log.i("Sound Controller", "Track has been completed.");
            }
        });

        YAMPApplication.getSoundController().setOnPlayingStartedListener(new PlayingStartedListener() {
            @Override
            public void onPlayingStarted() {
                Log.i("Sound Controller", "Track has been started.");
            }
        });
    }
}
