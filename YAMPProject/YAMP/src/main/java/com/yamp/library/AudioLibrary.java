package com.yamp.library;
import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Global library stores all audio files and playlists.
 * We can't have more than one audio library.
 */

public class AudioLibrary extends PlayList{
    private ArrayList<PlayList> playLists;
    private  ArrayList<PlayList> artists;
    private  ArrayList<PlayList> albums;



    public AudioLibrary() {
        super();
        this.playLists = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.albums = new ArrayList<>();
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

    public void insertAlbum(PlayList album) {
        this.albums.add(album);
    }
    public void insertArtist(PlayList artist) {
        this.artists.add(artist);
    }

    public ArrayList<PlayList> getArtists() {
        return artists;
    }

    public ArrayList<PlayList> getAlbums() {
        return albums;
    }
}
