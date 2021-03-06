package com.yamp.library;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import com.yamp.core.AudioManager;
import com.yamp.library.Adapters.AlbumsArtistsListAdapter;
import com.yamp.library.Adapters.CurrentListAdapter;
import com.yamp.library.Adapters.PlaylistEditorAdapter;
import com.yamp.library.Adapters.PlaylistsListAdapter;
import com.yamp.library.Adapters.SongsListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdYa on 24.11.13.
 * Controlling AudioLibrary content.
 */
public class AudioLibraryManager {

    private ContentResolver resolver;
    private AudioLibrary library;
    private PlayList currentlyEditedPlaylist;

    //data adapters.
    private SongsListAdapter songsListAdapter;
    private AlbumsArtistsListAdapter albumsListAdapter;
    private AlbumsArtistsListAdapter artistsListAdapter;
    private PlaylistsListAdapter playlistsListAdapter;
    private PlaylistEditorAdapter playlistEditorAdapter;
    private CurrentListAdapter currentListAdapter;

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

    //binds data adapters to specified activity.
    public void initDataAdapters(FragmentActivity activity) {
        this.songsListAdapter = new SongsListAdapter(activity);
        this.albumsListAdapter = new AlbumsArtistsListAdapter(getAlbums(), activity);
        this.artistsListAdapter = new AlbumsArtistsListAdapter(getArtists(), activity);
        this.playlistsListAdapter = new PlaylistsListAdapter(getPlaylists(), activity);
        this.playlistEditorAdapter = new PlaylistEditorAdapter(activity);
        this.currentListAdapter = new CurrentListAdapter(AudioManager.getInstance().getCurrentPlayList(), activity);
    }

    public void scanForPlaylists(){
        library.clearPlaylists();
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (validateCursor(cursor)) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists._ID);
            int nameColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME);
            do {
                long ID = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                library.insertPlaylist(new PlayList(name, ID));
                scanSongsForPlaylist(ID);
            } while (cursor.moveToNext());
        }
        // update currently edited playlist reference.
        if(currentlyEditedPlaylist != null){
            setCurrentlyEditedPlaylist(currentlyEditedPlaylist.getName());
        }
    }

    public long getPlaylistID(String playlistName){
        for (PlayList playList : library.getPlayLists()) {
            if (playList.getName().equals(playlistName))
                return playList.getID();
        }
        return -1;
    }

    public boolean isPlaylistExists(String name){
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (validateCursor(cursor)) {
            int nameColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME);
            do {
                String playlistsName = cursor.getString(nameColumn);
                if (name.equals(playlistsName))
                    return true;
            } while (cursor.moveToNext());
        }
        return false;
    }

    public boolean addPlaylist(String name){
        if(name == null || isPlaylistExists(name))
            return false;

        ContentValues cValues = new ContentValues();
        cValues.put(MediaStore.Audio.Playlists.NAME, name);
        resolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, cValues);

        return true;
    }

    public void scanSongsForPlaylist(long playlistID){
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (validateCursor(cursor)) {
            int columnTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int columnArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int columnAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int columnDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int columnData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int columnAlbumID = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            do {
                long id = cursor.getLong(columnID);
                long albumID = cursor.getLong(columnAlbumID);
                String title = cursor.getString(columnTitle);
                String artist = cursor.getString(columnArtist);
                String album = cursor.getString(columnAlbum);
                int duration = cursor.getInt(columnDuration);
                String data = cursor.getString(columnData);
                library.insertTrackIntoPlaylist(playlistID, new AudioFile(id, title, artist, album, albumID, duration, data));
            } while (cursor.moveToNext());
        }
    }

    public void scanForAllSongs(){
        library.clearSongs();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);

        if(validateCursor(cursor)){
            int columnTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int columnArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int columnAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int columnDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int columnData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int columnAlbumID = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do{
                long id = cursor.getLong(columnID);
                long albumID = cursor.getLong(columnAlbumID);
                String title = cursor.getString(columnTitle);
                String artist = cursor.getString(columnArtist);
                String album = cursor.getString(columnAlbum);
                int duration = cursor.getInt(columnDuration);
                String data = cursor.getString(columnData);
                library.insertTrack(new AudioFile(id, title, artist, album, albumID, duration, data));
            } while(cursor.moveToNext());
        }
    }

    public void removePlaylist(String name){
        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        final String [] playlistID = { MediaStore.Audio.Playlists._ID };
        final String[] playlistName = { name };
        Cursor cursor = resolver.query(uri, playlistID, MediaStore.Audio.Playlists.NAME + "=?", playlistName, null);
        if (validateCursor(cursor)) {
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Playlists._ID);
            String [] ID = { cursor.getString(columnID) };
            resolver.delete(uri, MediaStore.Audio.Playlists._ID + "=?", ID);
        }
    }

    public void addSongToPlaylist(long playlistID, long songID){
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);

        ContentValues cValues = new ContentValues();
        cValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, 0);
        cValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songID);
        resolver.insert(uri, cValues);
    }

    public void removeSongFromPlaylist(long playlistID, long songID){
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);
        String[] args = { String.valueOf(songID) };
        resolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID + "=?", args);
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
        library.clearAlbums();
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
        library.clearArtists();
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

    public List<AudioFile> getAllTracks(){
        return this.library.getTracks();
    }

    public boolean adaptersAreReady(){
        if (songsListAdapter      == null ||
            albumsListAdapter     == null ||
            artistsListAdapter    == null ||
            playlistsListAdapter  == null ||
            playlistEditorAdapter == null ||
            currentListAdapter    == null)
            return false;
        return true;
    }

    public void notifyAllAdapters(){
        if (!adaptersAreReady())
            return;

        songsListAdapter.notifyDataSetChanged();
        albumsListAdapter.notifyDataSetChanged();
        currentListAdapter.notifyDataSetChanged();
        artistsListAdapter.notifyDataSetChanged();
        playlistsListAdapter.notifyDataSetChanged();
        playlistEditorAdapter.notifyDataSetChanged();
    }

    public PlayList getPlaylistByID(long playlistID){
        for (PlayList playList : library.getPlayLists()) {
            if (playList.getID() == playlistID)
                return playList;
        }
        return null;
    }

    public PlaylistsListAdapter getPlaylistsListAdapter() {
        return playlistsListAdapter;
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

    public SongsListAdapter getSongsListAdapter() {
        return songsListAdapter;
    }

    public CurrentListAdapter getCurrentListAdapter() {
        return currentListAdapter;
    }

    public AlbumsArtistsListAdapter getAlbumsListAdapter() {
        return albumsListAdapter;
    }

    public AlbumsArtistsListAdapter getArtistsListAdapter() {
        return artistsListAdapter;
    }

    public PlaylistEditorAdapter getPlaylistEditorAdapter() {
        return playlistEditorAdapter;
    }

    public PlayList getLibrary(){
        return library;
    }

    public PlayList getCurrentlyEditedPlaylist() {
        return currentlyEditedPlaylist;
    }

    public void setCurrentlyEditedPlaylist(String currentlyEditedPlaylist) {
        for (PlayList playList : library.getPlayLists()) {
            if (playList.getName().equals(currentlyEditedPlaylist)){
                this.currentlyEditedPlaylist = playList;
                break;
            }
        }
    }
}
