package com.yamp.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.yamp.R;

import java.io.IOException;

/**
 * Created by AdYa on 24.11.13.
 *
 * Responsible for sound
 */
public class SoundController extends Service{

    private AudioStream currentStream;
    private MediaPlayer player;
    private boolean loaded = false;

    // Does low-level stuff for playing sound :)

    public void initialize(){ ///TODO: Uninitiaalized player here
        player = new MediaPlayer();
        Log.i("Sound Controller", "Service STARTED");
    }
    public void destroy(){
        player.stop();
        player.release();
        player = null;
        Log.i("Sound Controller", "Service STOPED");
        stopSelf();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }


    /**
    *    Don't touch anything below :D
    **/

    private final IBinder binder = new SoundControllerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        initialize();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        destroy();
        return true;
    }

    public void pause(){
        player.pause();
    }
    public void play() {
        if (!loaded){
            loaded=true;
            player = MediaPlayer.create(getApplicationContext(),R.raw.test);
        }
        player.start();
    }

    public class SoundControllerBinder extends Binder{
        public SoundController getService() {
            // Return this instance of LocalService so clients can call public methods
            return SoundController.this;
        }
    }
}
