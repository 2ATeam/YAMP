package com.yamp.library;

/**
 * Created by AdYa on 24.11.13.
 *
 * Stores all information about audio file
 */
public class AudioFile {

    private String name;
    private String albumn;
    private String artist;
    private String genre;
    private String lyrics; // stores huge text :)
    private int year;

    private int duration;
    private String path; // There is a Path class for this? :D
    private int rank;

    public AudioFile(String name, String artist, String album, int duration) {
        this.name = name;
        this.artist = artist;
        this.albumn = album;
        this.duration = duration;
    }
}
