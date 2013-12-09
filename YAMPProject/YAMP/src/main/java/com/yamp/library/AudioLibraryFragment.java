package com.yamp.library;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.yamp.R;


import java.util.ArrayList;

import com.yamp.core.AudioManager;

/**
 * Created by Lux on 07.12.13.
 */

public class AudioLibraryFragment extends Fragment {

    FragmentActivity activity; // parent activity reference.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_library_fragment_tabs, container, false);
        createTabs(view);
        return view;
    }

    private void createTabs(View view){
        TabHost host = (TabHost) view.findViewById(R.id.tabHost);
        host.setup();
        addTab(host, "ALL", R.id.tabAll);
        addTab(host, "PLAYLISTS", R.id.tabPlaylists);
        addTab(host, "ALBUMS", R.id.tabAlbums);
        addTab(host, "ARTISTS", R.id.tabArtists);
    }

    private void addTab(TabHost host, String indicator, int resID){
        TabHost.TabSpec spec = host.newTabSpec(indicator.toLowerCase());
        spec.setContent(resID);
        spec.setIndicator(indicator);
        host.addTab(spec);
    }

    private void populateLists() {
        //get views
        ListView view = (ListView) activity.findViewById(R.id.list_all_tracks);
        ExpandableListView albumsListView = (ExpandableListView) activity.findViewById(R.id.list_all_albums);
        ExpandableListView artistListView = (ExpandableListView) activity.findViewById(R.id.list_all_artists);

        //set adapters
        view.setAdapter(new SongsListAdapter());
        albumsListView.setAdapter(new AlbumsArtistsListAdapter(AudioLibraryManager.getInstance().getAlbums()));
        artistListView.setAdapter(new AlbumsArtistsListAdapter(AudioLibraryManager.getInstance().getArtists()));

        //awake controls
        registerTouchHandler();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity(); // we can get the reference to activity only then it was created. Not earlier!!

        populateLists();
    }

    private void registerTouchHandler(){
        ListView view = (ListView) activity.findViewById(R.id.list_all_tracks);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                PlayList pl = AudioLibraryManager.getInstance().getLibrary();
                pl.setCurrent(pos);
                AudioManager.getInstance().setPlayList(pl);
                AudioManager.getInstance().playTrack();
            }
        });
    }

    private View getSongView(AudioFile track, View view){
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

    private class SongsListAdapter extends ArrayAdapter<AudioFile> {

        public SongsListAdapter() {
            super(activity, R.layout.audio_library_list_entry, AudioLibraryManager.getInstance().getAllTracks());
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
    }

    private class AlbumsArtistsListAdapter extends BaseExpandableListAdapter {

        ArrayList<PlayList> albumArtists;

        private AlbumsArtistsListAdapter(ArrayList<PlayList> albumArtists) {
            this.albumArtists = albumArtists;
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
        public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
            if(convertView == null){
               convertView = activity.getLayoutInflater().inflate(R.layout.audio_library_albumartist_entry, viewGroup, false);
            }

            PlayList album = albumArtists.get(i);
            TextView albumName = (TextView) convertView.findViewById(R.id.txtAlbumartistName);
            TextView albumSongsAmount = (TextView) convertView.findViewById(R.id.txtAlbunartistAmount);
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
        public boolean isChildSelectable(int i, int i2) {
            return true;
        }
    }
}