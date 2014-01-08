package com.yamp.events;


/**
 * Created by AdYa on 03.12.13.
 */
public interface PlaybackListener {
    public void onPlayingStarted(boolean causedByUser);
    public void onPlayingPaused(int currentProgress);
    public void onPlayingResumed(int currentProgress);
    public void onPlayingCompleted(boolean causedByUser);

}
