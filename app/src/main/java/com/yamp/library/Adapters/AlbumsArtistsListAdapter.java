package com.yamp.library.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.core.YAMPApplication;
import com.yamp.library.AlbumArtLoader;
import com.yamp.library.AudioFile;
import com.yamp.library.PlayList;
import com.yamp.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by Lux on 23.12.13.
 */
public class AlbumsArtistsListAdapter extends BaseExpandableListAdapter implements ISongsDisplayable{

    private FragmentActivity activity;
    private ArrayList<PlayList> albumArtists;

    public AlbumsArtistsListAdapter(ArrayList<PlayList> albumArtists, FragmentActivity activity) {
        this.albumArtists = albumArtists;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        return albumArtists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return albumArtists.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return albumArtists.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return albumArtists.get(i).getTrack(i2);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.audio_library_albumartist_entry, viewGroup, false);
        }

        PlayList album = albumArtists.get(i);
        TextView albumName = (TextView) convertView.findViewById(R.id.txtAlbumartistName);
        TextView albumSongsAmount = (TextView) convertView.findViewById(R.id.txtAlbunartistAmount);
        ImageView albumCover = (ImageView) convertView.findViewById(R.id.albumartistPicture);
        albumCover.setImageBitmap(AlbumArtLoader.getArtwork(YAMPApplication.getInstance(),album.getID()));
        albumName.setText(album.getName());
        albumSongsAmount.setText(String.valueOf(album.size()));
        return convertView;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.audio_library_list_entry, viewGroup, false);
        }
        AudioFile track = albumArtists.get(i).getTrack(i2);
        return getSongView(track, convertView);
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

        ImageView playingIndicator = (ImageView)view.findViewById(R.id.imgIsPlaing);
        AudioFile playingNow = AudioManager.getInstance().getCurrent();
        if(track.getID() == playingNow.getID())
            playingIndicator.setVisibility(View.VISIBLE);
        else
            playingIndicator.setVisibility(View.INVISIBLE);

        return view;
    }
}
