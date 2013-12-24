package com.yamp.library;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by AdYa on 24.11.13.
 * Controlling AudioLibrary content.
 */
public class AudioLibraryManager {

    private ContentResolver resolver;
    private AudioLibrary library;
    private static AudioLibraryManager instance;

    private AudioLibraryManager() {
        library = new AudioLibrary();
    }

    public static AudioLibraryManager getInstance(){
        if(instance == null){
            instance = new AudioLibraryManager();
        }
        return instance;
    }

    public void scanForPlaylists(){
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (validateCursor(cursor)) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists._ID);
            int nameColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME);
            do {
                long ID = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                library.insertPlaylist(new PlayList(name, ID));
            } while (cursor.moveToNext());
        }
    }

    public void addPlaylist(String name){
        if(name == null)
            return;

        ContentValues cValues = new ContentValues();
        cValues.put(MediaStore.Audio.Playlists.NAME, name);
        resolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cValues);
    }

    public void removePlaylist(String name){
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (validateCursor(cursor)) {
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Playlists._ID);
            String [] ID = { cursor.getString(columnID) };
            resolver.delete(uri, MediaStore.Audio.Playlists._ID + "=?", ID);
            scanForPlaylists(); // perform rescan;
        }
    }

    public void scanSongsForPlaylist(long playlistID){
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (validateCursor(cursor)) {
            ///TODO: implement this.
        }
    }

    public void addSongToPlaylist(long playlistID, long songID){
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if(!validateCursor(cursor)){
            final int base = cursor.getInt(0);
            cursor.close();
            ContentValues cValues = new ContentValues();
            cValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, base + 1);
            cValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songID);
            resolver.insert(uri, cValues);
        }
    }

    public void scanForAllSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if(validateCursor(cursor)){
            int columnTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int columnArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int columnAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int columnDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int columnData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                long id = cursor.getLong(columnID);
                String title = cursor.getString(columnTitle);
                String artist = cursor.getString(columnArtist);
                String album = cursor.getString(columnAlbum);
                int duration = cursor.getInt(columnDuration);
                String data = cursor.getString(columnData);
                library.insertTrack(new AudioFile(id, title, artist, album, duration, data));
            } while(cursor.moveToNext());
        }
    }

    private boolean validateCursor(Cursor cursor){
        if(cursor == null){
            System.err.println("Media resolving query failed.");
            return false;
        }
        else if(!cursor.moveToFirst()){
            System.err.println("No entries resolved.");
            return false;
        }
        return true;
    }

    public void scanForAlbums(){
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if(!validateCursor(cursor)){
            return;
        }
        else{
            int columnName = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            do{
                String albumName = cursor.getString(columnName);
                long albumID = cursor.getLong(columnID);
                PlayList album = new PlayList(albumName, albumID);
                for(AudioFile song : library.getTracks()){
                    if(song.getAlbum().equals(albumName))
                        album.addTrack(song);
                }
                library.insertAlbum(album);
            }while (cursor.moveToNext());
        }
    }

    public void scanForArtists(){
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if(validateCursor(cursor)){
            int columnName = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            do{
                String artistName = cursor.getString(columnName);
                long artistID = cursor.getLong(columnID);
                PlayList artist = new PlayList(artistName, artistID);
                for (AudioFile song : library.getTracks()){
                    if(song.getArtist().equals(artistName))
                        artist.addTrack(song);
                }
                library.insertArtist(artist);
            }while (cursor.moveToNext());
        }
    }

    public void performFullScan(){
        scanForAllSongs();
        scanForAlbums();
        scanForArtists();
        scanForPlaylists();
    }

    public void setResolver(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public ArrayList<AudioFile> getAllTracks(){
        return this.library.getTracks();
    }

    public ArrayList<String> getAlbumNames(){
        ArrayList<String> names = new ArrayList<>();
        for (PlayList album : library.getAlbums()) {
            names.add(album.getName());
        }
        return names;
    }

    public ArrayList<PlayList> getAlbums(){
        return library.getAlbums();
    }

    public ArrayList<PlayList> getArtists(){
        return library.getArtists();
    }

    public ArrayList<PlayList> getPlaylists(){
        return library.getPlayLists();
    }

    public AudioFile getTrack(int index){
        return library.getTrack(index);
    }

    public PlayList getLibrary(){
        return library;
    }
}
