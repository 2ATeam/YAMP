package com.yamp.events;

import com.yamp.library.AudioFile;

/**
 * Created by AdYa on 07.12.13.
 */
public interface TrackLoadedListener {
    public void onNewTrackLoaded(AudioFile track);
    public void onNextTrackLoaded(AudioFile nextTrack);
    public void onPrevTrackLoaded(AudioFile prevTrack);
}
