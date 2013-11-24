package com.yamp.library;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Stores all audio files to play.
 */
public class PlayList {
    protected ArrayList<AudioFile> tracks;

    public int size(){
        return tracks.size();
    }
}
