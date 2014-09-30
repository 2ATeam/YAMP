package com.yamp.core;

import com.yamp.events.PlaybackListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.events.TrackLoadedListener;
import com.yamp.library.AudioFile;
import com.yamp.library.AudioLibraryManager;
import com.yamp.library.PlayList;
import com.yamp.sound.SoundController;
import com.yamp.utils.LoopButton;
import com.yamp.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 * <p/>
 * Responses for common operations. (Activity?)
 */
public class AudioManager {


    private boolean looped = false;

    private boolean shuffle = false;
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
        YAMPApplication.addFirstSoundControllerBoundedListener(new SoundControllerBoundedListener() {
            @Override
            public void onSoundControllerBounded(SoundController controller) {
                AudioManager.this.controller = controller;
                AudioManager.this.controller.setPlaybackListener(new PlaybackListener() {
                    @Override
                    public void onPlayingStarted(boolean causedByUser) {
                    }

                    @Override
                    public void onPlayingCompleted(boolean causedByUser) {
                        if (!isLoopedTrack()) {// if track is not looped play next track.
                            int cur = trackList.getCurrent();
                            next();
                            boolean kostil = (cur == (trackList.size() - 1) && trackList.getCurrent() == 0) && !isLooped();
                            // kostil :
                            //          * if we reached last track
                            //          * and next() bring us to the first track
                            //          * and player isn't looped
                            // then we should not start playing... otherwise everything is OK
                            /// TODO: Hello from India! :DDD
                            if (readyToPlay && !kostil) playTrack();
                        }
                    }

                    @Override
                    public void onPlayingPaused(int currentProgress) {
                    }

                    @Override
                    public void onPlayingResumed(int currentProgress) {
                    }
                });
            }
        });
        trackList = new PlayList();
    }

    private void setTrack(AudioFile track){
        String path = track.getPath();
        if (controller.isPlaying()){
            controller.pause();
            controller.play(path);
        }
        else
            controller.setTrack(path);
        notifyNewTrackLoaded(track);
    }

    public void playTrack(){
        controller.play(trackList.getCurrentTrack().getPath());
        notifyNewTrackLoaded(trackList.getCurrentTrack());
    }

    public void play() {
        if (!readyToPlay){
            readyToPlay = true;
            setTrack(trackList.getCurrentTrack());
        }
        controller.play(); /// resume
    }

    public void pause() {
        controller.pause();
    }
    public void stop() {
        controller.stop();
        readyToPlay = false;
    }
    public void next() {
        if (shuffle) {
            int rnd = trackList.getCurrent();
            while(rnd == trackList.getCurrent()){
                rnd =  Utilities.randomInt(0, trackList.size()-1) - 1;
            }
            trackList.setCurrent(rnd);
        }

        if (!looped){
            if (trackList.getNext() == 0){
                pause();
               // return;
            }

        }
        setTrack(trackList.nextTrack());
        notifyNextTrackLoaded(trackList.getNextTrack());
    }

    public void prev() {
        if (shuffle) {
            int rnd = trackList.getCurrent();
            while(rnd == trackList.getCurrent()){
                rnd =  Utilities.randomInt(0, trackList.size() - 2) + 1;
            }
            trackList.setCurrent(rnd);
        }

        if (!looped){
            if (trackList.getPrev() == trackList.size() - 1){
                pause();
   //             return;
            }
        }
        setTrack(trackList.prevTrack());
        notifyPrevTrackLoaded(trackList.getPrevTrack());
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
        if (playlist != null && playlist.size() > 0){
            this.trackList = playlist;
            if (AudioLibraryManager.getInstance().adaptersAreReady()){
                AudioLibraryManager.getInstance().getCurrentListAdapter().setDataSource(trackList);
                AudioLibraryManager.getInstance().getCurrentListAdapter().notifyDataSetChanged();
            }
        }
    }

    public PlayList getCurrentPlayList(){
        return this.trackList;
    }

    public AudioFile getCurrent(){
        return trackList.getCurrentTrack();
    }

    public void seekTo(int msec) {
        controller.seekTo(msec);
    }

    public int getCurrentProgress(){
        return controller.getProgress();
    }
    public int getCurrentDuration(){
        return controller.getDuration();
    }

    public boolean isPlaying() {
        return controller.isPlaying();
    }
    public boolean isPaused() {
        return controller.isPaused();
    }

    public boolean isLoopedTrack(){
        return controller.isLooped();
    }

    public boolean isLooped() {
        return looped;
    }

    public boolean isShuffled() {
        return shuffle;
    }

    public void setLoopMode(int loopMode) {
        switch (loopMode){
            case LoopButton.STATE_NONE:
                this.looped = false;
                controller.setLooping(false);
                break;
            case LoopButton.STATE_SINGLE:
                this.looped = false;
                controller.setLooping(true);
                break;
            case LoopButton.STATE_ALL:
                controller.setLooping(false);
                this.looped = true;
                break;
        }
    }

    public int getLoopMode(){
        return (this.looped ? LoopButton.STATE_ALL : (controller.isLooped() ? LoopButton.STATE_SINGLE : LoopButton.STATE_NONE));
    }

    /**
     * Listeners ....
     **/

    private ArrayList<TrackLoadedListener> trackLoadedListeners = new ArrayList<>();

    public void setTrackLoadedListener(TrackLoadedListener listener){
        trackLoadedListeners.add(listener);
    }
    private void notifyNewTrackLoaded(AudioFile track){
        AudioLibraryManager.getInstance().notifyAllAdapters();
        for (TrackLoadedListener listener : trackLoadedListeners){
            listener.onNewTrackLoaded(track);
        }
    }
    private void notifyNextTrackLoaded(AudioFile track){
        for (TrackLoadedListener listener : trackLoadedListeners){
            listener.onNextTrackLoaded(track);
        }
    }

    private void notifyPrevTrackLoaded(AudioFile track){
        for (TrackLoadedListener listener : trackLoadedListeners){
            listener.onPrevTrackLoaded(track);
        }
    }

    // SoundController redirection
    public void setPlaybackListener(PlaybackListener listener) {
        controller.setPlaybackListener(listener);
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }
}
