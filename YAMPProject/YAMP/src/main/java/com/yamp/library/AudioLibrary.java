package com.yamp.library;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Global library stores all audio files and playlists.
 * We can't have more than one audio library.
 * It is a container for the all Media.
 * That's why, for the simplicity, it is implemented using singleton pattern.
 */

public class AudioLibrary extends PlayList{
    private ArrayList<PlayList> playLists;
    private static AudioLibrary instance;

    private AudioLibrary() {
        super();
        this.playLists = new ArrayList<PlayList>();
    }

    public static AudioLibrary getInstance(){
        if(instance == null){
            instance = new AudioLibrary();
        }
        return instance;
    }

    public void insertTrack(AudioFile track){
        this.tracks.add(track);
    }
}
