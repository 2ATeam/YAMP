package com.yamp.core;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.sound.SoundController;
import com.yamp.utils.GestureAdapter;
import com.yamp.utils.Logger;

import java.util.ArrayList;

/**
 * Created by AdYa on 02.12.13.
 */
public class YAMPApplication extends Application {
    private static YAMPApplication instance;

    public static boolean isPlayerReady() {
        return instance.soundController != null;
    }

    public static YAMPApplication getInstance() {
        return instance;
    }

    public static SoundController getSoundController() {
        return instance.soundController;
    }


    @Override
    public void onCreate() {
        instance = this;
        Logger.enable();///TODO: enable/disable Logging here
        bindSoundController();
    }

    private void bindSoundController() {
        bindService(new Intent(this, SoundController.class),
                mConnection,
                Context.BIND_AUTO_CREATE);
    }

    private SoundController soundController; // bounded service

    /**
     * Do not edit anything below :)
     * **/

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to SoundController, cast the IBinder and get SoundController instance
            SoundController.SoundControllerBinder binder = (SoundController.SoundControllerBinder) service;
            soundController = binder.getService();
            fireSoundControllerBounded();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            soundController = null;

        }
    };

    private ArrayList<SoundControllerBoundedListener> soundControllerBoundedListeners = new ArrayList<>();

    public static void setOnSoundControllerBoundedListener(SoundControllerBoundedListener listener) {
        instance.soundControllerBoundedListeners.add(listener);
    }

    private void fireSoundControllerBounded() {
        for (SoundControllerBoundedListener listener : soundControllerBoundedListeners) {
            listener.onSoundControllerBounded(soundController);
        }
    }
}
