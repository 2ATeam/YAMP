package com.yamp.library.adapters;
import android.support.v4.app.FragmentActivity;

import com.yamp.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamp.library.AudioFile;
import com.yamp.library.AudioLibraryManager;
import com.yamp.utils.Utilities;


/**
 * Created by Lux on 29.12.13.
 */
public class PlaylistEditorAdapter extends ArrayAdapter<AudioFile> implements ISongsDisplayable {

    private FragmentActivity activity; // parent activity reference.

    public PlaylistEditorAdapter(FragmentActivity activity) {
        super(activity, R.layout.playlist_editing_entry, AudioLibraryManager.getInstance().getAllTracks());
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null)
            view = activity.getLayoutInflater().inflate(R.layout.playlist_editing_entry, parent, false);

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
        duration.setText(String.valueOf(Utilities.formatTime(track.getDuration())));
        artist.setText(track.getArtist().trim());
        return view;
    }
}
