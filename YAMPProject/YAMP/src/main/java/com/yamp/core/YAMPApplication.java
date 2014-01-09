package com.yamp.core;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.sound.SoundController;
import com.yamp.utils.Logger;

import java.util.ArrayList;

/**
 * Created by AdYa on 02.12.13.
 */
public class YAMPApplication extends Application {
    private static YAMPApplication instance;

    private TelephonyManager telephony;
    private PhoneStateListener phoneStateListener;

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
        super.onCreate();
        instance = this;
        Logger.enable();///TODO: enable/disable Logging here
        bindSoundController();
        SessionSaver.load();

        MusicIntentReceiver receiver = new MusicIntentReceiver();
        registerReceiver(receiver, new IntentFilter(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY));
        registerReceiver(new CallIntentReceiver(), new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
        telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        phoneStateListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                }
            }
        };

        telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onTerminate() {
        SessionSaver.save();
        telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        super.onTerminate();
    }

    public class CallIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AudioManager.getInstance().pause();
        }
    }
    public class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isPlayerReady()) return;

            if (android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                AudioManager.getInstance().pause();
                Log.d("Mesage","Unplug");
                Toast.makeText(context, "Heello", Toast.LENGTH_LONG).show();
            }
        }
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
            notifySoundControllerBounded();
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

    ///TODO: Temporary fix for SLR bug.
    public static void addFirstSoundControllerBoundedListener(SoundControllerBoundedListener listener){
        instance.soundControllerBoundedListeners.add(0, listener);
    }

    private void notifySoundControllerBounded() {
        for (SoundControllerBoundedListener listener : soundControllerBoundedListeners) {
            listener.onSoundControllerBounded(soundController);
        }
    }
}
