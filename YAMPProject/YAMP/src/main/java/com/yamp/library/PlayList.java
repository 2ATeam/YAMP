package com.yamp.library;

import com.yamp.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdYa on 24.11.13.
 * Stores all audio files.
 */
public class PlayList {
    protected List<AudioFile> tracks;
    private String name;
    private long ID;
    protected int current = 0;

    public PlayList() {
        this.tracks = new ArrayList<>();
    }

    public PlayList(String name, long id){
        this();
        this.name = name;
        this.ID = id;
    }

    public long getID() {
        return ID;
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
        tracks.add(track);
    }

    public boolean contains(AudioFile track){
        if (tracks.contains(track))
            return true;
        return false;
    }

    // navigate through playlist
    public AudioFile nextTrack(){
        current = getNext();
        return tracks.get(current);
    }
    public AudioFile prevTrack(){
        current = getPrev();
        return tracks.get(current);
    }

    // simple access to relevant tracks (prev/cur/next)
    public AudioFile getNextTrack(){
        int next = getNext();
        return tracks.get(next);
    }
    public AudioFile getPrevTrack(){
        int prev = getPrev();
        return tracks.get(prev);
    }
    public AudioFile getCurrentTrack(){
        return tracks.get(Utilities.clamp(0, tracks.size()-1, current));
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }
    public int getNext(){
        return  (current + 1) % (size());
    }
    public int getPrev(){
        return ((current <= 0) ? size() - 1 : (current - 1) % (size()));
    }
}
