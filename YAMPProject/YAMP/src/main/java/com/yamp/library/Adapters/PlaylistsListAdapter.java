package com.yamp.library.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.core.PlayerMainActivity;
import com.yamp.library.AudioFile;
import com.yamp.library.AudioLibraryManager;
import com.yamp.library.PlayList;

import java.util.ArrayList;

/**
 * Created by Lux on 23.12.13.
 */
public class PlaylistsListAdapter extends BaseExpandableListAdapter implements ISongsDisplayable {
    private PlayerMainActivity activity;
    private ArrayList<PlayList> playLists;
    private ImageView btnRemovePlaylist;
    private ImageView btnEditPlaylist;

    public PlaylistsListAdapter(ArrayList<PlayList> playLists, FragmentActivity activity) {
        this.playLists = playLists;
        this.activity = (PlayerMainActivity)activity;
    }

    @Override
    public int getGroupCount() {
        return playLists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return playLists.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return playLists.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return playLists.get(i).getTrack(i2);
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
            convertView = activity.getLayoutInflater().inflate(R.layout.audio_library_playlist_entry, viewGroup, false);
        }

        PlayList playList = playLists.get(i);
        TextView playlistName = (TextView) convertView.findViewById(R.id.txtPlaylistName);
        TextView playlistSongsAmount = (TextView) convertView.findViewById(R.id.txtAlbunartistAmount);
        btnRemovePlaylist = (ImageView) convertView.findViewById(R.id.btnRemove);
        btnEditPlaylist = (ImageView) convertView.findViewById(R.id.imgEditPlaylist);
        registerTouchHandlers(convertView);
        playlistName.setText(playList.getName());
        playlistSongsAmount.setText(String.valueOf(playList.size()));
        return convertView;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.audio_library_list_entry, viewGroup, false);
        }
        AudioFile track = playLists.get(i).getTrack(i2);
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
        duration.setText(String.valueOf(track.getDuration()));
        artist.setText(track.getArtist().trim());

        ImageView playingIndicator = (ImageView)view.findViewById(R.id.imgIsPlaing);
        AudioFile playingNow = AudioManager.getInstance().getCurrent();
        if(track.getID() == playingNow.getID())
            playingIndicator.setVisibility(View.VISIBLE);
        else
            playingIndicator.setVisibility(View.INVISIBLE);

        return view;
    }

    private void registerTouchHandlers(View cv){
        final TextView text = (TextView) cv.findViewById(R.id.txtPlaylistName);
        btnRemovePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioLibraryManager.getInstance().removePlaylist(text.getText().toString());
            }
        });

        btnEditPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioLibraryManager.getInstance().setCurrentlyEditedPlaylist(text.getText().toString());
                activity.replace(activity.getPlaylistEditorFragment(), new int[]{R.animator.slide_in_top,
                                                                                 R.animator.slide_out_bottom,
                                                                                 R.animator.slide_in_bottom,
                                                                                 R.animator.slide_out_top});
            }
        });
    }
}
