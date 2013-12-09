package com.yamp.library;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Host Playlist. Stores all audio files.
 */
public class PlayList {
    protected ArrayList<AudioFile> tracks;
    private String name;
    private long ID;

    public PlayList() {
        this.tracks = new ArrayList<AudioFile>();
    }

    public PlayList(String name, long id){
        this();
        this.name = name;
        this.ID = id;
    }

    public int size(){
        return tracks.size();
    }

    public String getName() {
        return name;
    }

    public AudioFile getTrack(int index){
        return tracks.get(index);
    }

    public void addTrack(AudioFile track){
        this.tracks.add(track);
    }

    public long getID() {
        return ID;
    }
}