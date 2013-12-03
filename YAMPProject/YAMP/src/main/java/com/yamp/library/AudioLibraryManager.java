package com.yamp.library;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by AdYa on 24.11.13.
 *
 *  Does something with AudioLibrary
 */
public class AudioLibraryManager {

    private ContentResolver resolver;

    public AudioLibraryManager(ContentResolver contentResolver) {
        this.resolver = contentResolver;
    }

    public void scan(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if(cursor == null){
            System.err.println("media resolving query failed.");
            return;
        }
        else if(!cursor.moveToFirst()){
            System.err.println("No entries resolved.");
            return;
        }
        else{
            int columnTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int columnID = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int columnArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int columnAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int columnDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do{
                long id = cursor.getLong(columnID);
                String title = cursor.getString(columnTitle);
                String artist = cursor.getString(columnArtist);
                String album = cursor.getString(columnAlbum);
                int duration = cursor.getInt(columnDuration);
                AudioLibrary.getInstance().insertTrack(new AudioFile(title, artist, album, duration));
            } while(cursor.moveToNext());
        }
    }
}
