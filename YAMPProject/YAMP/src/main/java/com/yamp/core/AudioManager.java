package com.yamp.core;
import com.yamp.events.NewTrackLoadedListener;
import com.yamp.events.PlayingCompletedListener;
import com.yamp.events.PlayingStartedListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.library.AudioFile;
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

    private boolean readyToPlay = false;
    private PlayList trackList; // target playlist

    private SoundController controller;
    private static AudioManager instance;

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
                AudioManager.this.controller.setOnPlayingCompletedListener(new PlayingCompletedListener() {
                    @Override
                    public void onPlayingCompleted() {
                       if (!AudioManager.this.controller.isLooped()){
                           next();
                           if (readyToPlay) play();// if track is not looped play next track.
                           ///TODO: kostil
                       }
                    }
                });
            }
        });
        trackList = new PlayList();
    }

    private void setTrack(AudioFile track){
        String path = track.getPath();
        if (controller.isPlaying())
            controller.play(path); ///TODO: Change getName() to getPath()
        else
            controller.setTrack(path);

        fireNewTrackLoaded(track);
    }

    public void playTrack(){
        controller.play(trackList.getCurrent().getPath());
        fireNewTrackLoaded(trackList.getCurrent());
    }

    public void play() {
        if (!readyToPlay){
            readyToPlay = true;
            playTrack();
        }
        controller.play();
    }
    public void pause() {
        controller.pause();
    }
    public void stop() {
        controller.stop();
    }

    public void next() {
        setTrack(trackList.nextTrack());
    }
    public void prev() {
       setTrack(trackList.prevTrack());
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

    public AudioFile getCurrent(){
        return trackList.getCurrent();
    }

    public boolean isPlaying() {
        return controller.isPlaying();
    }
    public boolean isLooped(){
        return controller.isLooped();
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
