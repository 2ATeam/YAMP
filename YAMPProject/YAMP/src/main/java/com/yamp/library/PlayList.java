package com.yamp.library;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Host Playlist. Stores all audio files.
 */
public class PlayList {
    protected ArrayList<AudioFile> tracks;

    public PlayList() {
        this.tracks = new ArrayList<AudioFile>();
    }

    public int size(){
        return tracks.size();
    }
}
