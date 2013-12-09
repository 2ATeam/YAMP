package com.yamp.core;

import com.yamp.events.NewTrackLoadedListener;
import com.yamp.events.PlayingCompletedListener;
import com.yamp.events.PlayingStartedListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.library.AudioFile;
import com.yamp.library.AudioLibrary;
import com.yamp.library.PlayList;
import com.yamp.sound.SoundController;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 * <p/>
 * Responses for common operations. (Activity?)
 */
public class AudioManager {


    private boolean looped;
    private boolean shuffle;
    private PlayList trackList; // target playlist

    /// TODO: TESTING THINGS....REPLACE IT WITH EXISTED TRACK
    private AudioFile current = new AudioFile("/sdcard/Music/test.mp3");


    private SoundController controller;
    private static AudioManager instance;
    private boolean readyToPlay = false;

    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    private AudioManager() {
        instance = this;
        YAMPApplication.setOnSoundControllerBoundedListener(new SoundControllerBoundedListener() {
            @Override
            public void onSoundControllerBounded(SoundController controller) {
               AudioManager.this.controller = controller;
            }
        });
        trackList = new PlayList();
        //trackList.addTrack(current);
    }

    private void setTrack(AudioFile track){
        String name = track.getName();
        readyToPlay = true;
        current = track;
        if (controller.isPlaying())
            controller.play(name); ///TODO: Change getName() to getPath()
        else if (controller.isPaused())
            controller.setTrack(name);
        else

        fireNewTrackLoaded(track);
    }


    public void play() {
        if (!readyToPlay){
            controller.play(current.getPath());
            readyToPlay = true;
        }
        else controller.play();
        fireNewTrackLoaded(current);
    }
    public void pause() {
        controller.pause();
    }
    public void stop() {
        controller.stop();
    }

    public void next() {
        //setTrack(trackList.nextTrack());
    }
    public void prev() {
       //setTrack(trackList.prevTrack());
    }

    public void seekTo(int msec) {
        controller.seekTo(msec);
    }
    public int getDuration() {
        return controller.getDuration();
    }

    public int getVolumeMax(){
        return SoundController.MAX_VOLUME;
    }
    public int getVolume(){
        return controller.getVolume();
    }
    public void setVolume(int volume) {
        controller.setVolume(volume);
    }

    public void setPlayList(PlayList playlist) {
        if (playlist != null && playlist.size() > 0)
            this.trackList = playlist;
    }

    public boolean isPlaying() {
        return controller.isPlaying();
    }

    public void setLooping(boolean looped) {
        controller.setLooping(looped);
    }


    /**
     * Listeners ....
     **/

    private ArrayList<NewTrackLoadedListener> newTrackLoadedListeners = new ArrayList<>();
    private void fireNewTrackLoaded(AudioFile track){
        for (NewTrackLoadedListener listener : newTrackLoadedListeners){
            listener.onNewTrackLoaded(track);
        }
    }
    public void setOnNewTrackLoadedListener(NewTrackLoadedListener listener){
        newTrackLoadedListeners.add(listener);
    }

    // SoundController redirection
    public void setOnPlayingStartedListener(PlayingStartedListener listener) {
        controller.setOnPlayingStartedListener(listener);
    }
    public void setOnPlayingCompletedListener(PlayingCompletedListener listener) {
        controller.setOnPlayingCompletedListener(listener);
    }

}
