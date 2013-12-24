package com.yamp.library.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.library.AudioFile;
import com.yamp.library.AudioLibraryManager;

/**
 * Created by Lux on 23.12.13.
 */

public class SongsListAdapter extends ArrayAdapter<AudioFile> implements ISongsDisplayable{

    private FragmentActivity activity; // parent activity reference.

    public SongsListAdapter(FragmentActivity activity) {
        super(activity, R.layout.audio_library_list_entry, AudioLibraryManager.getInstance().getAllTracks());
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = activity.getLayoutInflater().inflate(R.layout.audio_library_list_entry, parent, false);
        }

        AudioFile track = AudioLibraryManager.getInstance().getTrack(position);
            return getSongView(track, view);
    }

    @Override
    public View getSongView(AudioFile track, View view){
        TextView songName = (TextView) view.findViewById(R.id.song_name);
        TextView albumName = (TextView) view.findViewById(R.id.txtAlbum);
        TextView duration = (TextView) view.findViewById(R.id.txtDuration);
        TextView artist = (TextView) view.findViewById(R.id.txtArtist);
        songName.setText(track.getName().trim());
        albumName.setText(track.getAlbum().trim());
        duration.setText(String.valueOf(track.getDuration()));
        artist.setText(track.getArtist().trim());
        return view;
    }
}