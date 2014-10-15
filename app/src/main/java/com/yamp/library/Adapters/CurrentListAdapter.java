package com.yamp.library.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.library.AudioFile;
import com.yamp.library.PlayList;
import com.yamp.utils.Utilities;

/**
 * Created by Lux on 08.01.14.
 */
public class CurrentListAdapter  extends BaseAdapter implements ISongsDisplayable {

    private FragmentActivity activity; // parent activity reference.
    private PlayList currentPlaylist;

    public CurrentListAdapter(PlayList currentPlaylist, FragmentActivity activity) {
        this.activity = activity;
        this.currentPlaylist = currentPlaylist;
    }

    @Override
    public int getCount() {
        return currentPlaylist.size();
    }

    @Override
    public Object getItem(int i) {
        return currentPlaylist.getTrack(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setDataSource(PlayList playList){
        this.currentPlaylist = playList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null)
            view = activity.getLayoutInflater().inflate(R.layout.audio_library_list_entry, parent, false);

        AudioFile track = currentPlaylist.getTrack(position);
        return getSongView(track, view);
    }

    @Override
    public View getSongView(AudioFile track, View view) {
        TextView songName = (TextView) view.findViewById(R.id.song_name);
        TextView albumName = (TextView) view.findViewById(R.id.txtAlbum);
        TextView duration = (TextView) view.findViewById(R.id.txtDuration);
        TextView artist = (TextView) view.findViewById(R.id.txtArtist);
        songName.setText(track.getName().trim());
        albumName.setText(track.getAlbum().trim());
        duration.setText(String.valueOf(Utilities.formatTime(track.getDuration())));
        artist.setText(track.getArtist().trim());

        ImageView playingIndicator = (ImageView)view.findViewById(R.id.imgIsPlaing);
        AudioFile playingNow = AudioManager.getInstance().getCurrent();
        if(track.getID() == playingNow.getID())
            playingIndicator.setVisibility(View.VISIBLE);
        else
            playingIndicator.setVisibility(View.INVISIBLE);

        return view;
    }
}
