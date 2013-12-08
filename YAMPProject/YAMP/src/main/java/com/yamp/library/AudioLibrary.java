package com.yamp.library;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Global library stores all audio files and playlists.
 * We can't have more than one audio library.
 */

public class AudioLibrary extends PlayList{
    private ArrayList<PlayList> playLists;

    public AudioLibrary() {
        super();
        this.playLists = new ArrayList<PlayList>();
    }

    public ArrayList<AudioFile> getTracks(){
        return this.tracks;
    }

    public ArrayList<PlayList> getPlayLists() {
        return playLists;
    }

    public AudioFile getTrack(int index) {
        return tracks.get(index);
    }

    public void insertTrack(AudioFile track){
        this.tracks.add(track);
    }

    public void insertPlaylist(PlayList playList){
        this.playLists.add(playList);
    }
}
