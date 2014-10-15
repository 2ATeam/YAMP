package com.yamp.library;

import com.yamp.utils.Utilities;

/**
 * Created by AdYa on 24.11.13.
 *
 * Stores all information about audio file
 */
public class AudioFile {

    private String name;
    private String album;
    private String artist;

    private String genre;
    private String lyrics; // stores huge text :)
    private int year;

    private int duration;
    private String path;
    private int rank;
    private long ID;
    private long albumID;
    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public AudioFile(long id, String name, String artist, String album, long albumID, int duration, String path) {
        this.ID = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.isPlaying = false;
        this.albumID = albumID;
    }

    public long getAlbumID() {
        return albumID;
    }

    public AudioFile(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public String getFormattedDuration(){
        return Utilities.formatTime(duration);
    }

    public String getPath() {
        return path;
    }

    public long getID() {
        return ID;
    }
}
