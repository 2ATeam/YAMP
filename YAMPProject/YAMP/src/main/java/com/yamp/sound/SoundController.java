package com.yamp.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.yamp.events.PlaybackListener;
import com.yamp.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Responsible for sound
 */
public class SoundController extends Service{

    private MediaPlayer player;

    private boolean prepared = false;
    private boolean paused = false;
    private boolean looped = false;

    ///TODO: REPLAY?
    public final static float MAX_PROGRESS_FOR_REPLAY = 2.5f; // Playing progress in percents before track will be restarted on PREV command
    public final static int MAX_VOLUME = 20; // Scales volume to [0.0; 1.0] range
    private int currentVolume = MAX_VOLUME / 2;

    public void initialize(){
        player = new MediaPlayer();
        setVolume(currentVolume);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                notifyPlayingCompleted(false);
                if (looped){
                    seekTo(0);
                    play();
                }
            }
        });
    }
    public void destroy(){
        player.stop();
        player.release();
        player = null;
        stopSelf();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }
    public boolean isLooped(){
        return looped;
    }
    public boolean isPaused() { return paused; }

    private void stopInternal(){
        paused = false;
        player.stop();
    }
    public void stop(){
        stopInternal();
        notifyPlayingCompleted(true);
    }

    public void pause(){
        if (isPlaying()){
            player.pause();
            paused = true;
            notifyPlayingPaused(getProgress());
        }
    }

    public void play() {
        if (!prepared) return;
        if (isPlaying()) return;

        if (paused) notifyPlayingResumed(getProgress());
        else notifyPlayingStarted(true); /// TODO: not exactly) here we must separate external play and internal play like stop :)
        paused = false;
        player.start();

    }

    // Sets new track and immediately starts it.
    public void play(String path){
        if (isPlaying()) stopInternal();
        setTrack(path);
        play();
    }

    // Sets new track, but won't play it.
    // Note: it won't set the track if some other track has already been playing.
    public void setTrack(String path) {
        if (isPlaying()) return;
        if (paused) stopInternal();
        try {
            player.reset();
            player.setDataSource(path);
            player.prepare();
            prepared = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLooping(boolean looping){
        this.looped = looping;
    }
    public void seekTo(int msec){
        player.seekTo(Utilities.clamp(0, getDuration(), msec));
    }

    public int getDuration(){
        return player.getDuration();
    }

    public int getProgress(){
        return player.getCurrentPosition();
    }

    public float getProgressPercent(){
        return (float)getProgress() / (float)getDuration() * 100.f;
    }
    private float scaleVolume(int nonScaledVolume){
        return 1f - (float)(Math.log(MAX_VOLUME - nonScaledVolume)/Math.log(MAX_VOLUME)); // V = 1 - ln(max - x) / ln(max)
    }
    public void setVolume(int volume){
        currentVolume = Utilities.clamp(0, MAX_VOLUME - 1, volume);
        float scaledVolume = scaleVolume(currentVolume);
        player.setVolume(scaledVolume, scaledVolume);
    }



    public int getVolume(){
        return currentVolume;
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

    public class SoundControllerBinder extends Binder{
        public SoundController getService() {
            // Return this instance of LocalService so clients can call public methods
            return SoundController.this;
        }
    }


    private ArrayList<PlaybackListener> playbackListeners = new ArrayList<>();
    public void setPlaybackListener(PlaybackListener listener){
        playbackListeners.add(listener);
    }
    private void notifyPlayingCompleted(boolean causedByUser){
        for (PlaybackListener listener : playbackListeners){
            listener.onPlayingCompleted(causedByUser);
        }
    }
    private void notifyPlayingStarted(boolean causedByUser){
        for (PlaybackListener listener : playbackListeners){
            listener.onPlayingStarted(causedByUser);
        }
    }
    private void notifyPlayingPaused(int currentProgress){
        for (PlaybackListener listener : playbackListeners){
            listener.onPlayingPaused(currentProgress);
        }
    }
    private void notifyPlayingResumed(int currentProgress){
        for (PlaybackListener listener : playbackListeners){
            listener.onPlayingResumed(currentProgress);
        }
    }




}
