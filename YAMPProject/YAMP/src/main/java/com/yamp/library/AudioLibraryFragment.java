package com.yamp.library;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ExpandableListView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;

import com.yamp.R;


import com.yamp.core.AudioManager;
import com.yamp.library.Adapters.AlbumsArtistsListAdapter;
import com.yamp.library.Adapters.PlaylistsListAdapter;
import com.yamp.library.Adapters.SongsListAdapter;
import com.yamp.utils.PromptDialog;

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
        ListView allTracksView = (ListView) activity.findViewById(R.id.list_all_tracks);
        ExpandableListView albumsListView = (ExpandableListView) activity.findViewById(R.id.list_all_albums);
        ExpandableListView artistListView = (ExpandableListView) activity.findViewById(R.id.list_all_artists);
        ExpandableListView playlistsListView = (ExpandableListView) activity.findViewById(R.id.list_custom_playlists);

        //set adapters
        allTracksView.setAdapter(new SongsListAdapter(getActivity()));
        albumsListView.setAdapter(new AlbumsArtistsListAdapter(AudioLibraryManager.getInstance().getAlbums(), getActivity()));
        artistListView.setAdapter(new AlbumsArtistsListAdapter(AudioLibraryManager.getInstance().getArtists(), getActivity()));
        playlistsListView.setAdapter(new PlaylistsListAdapter(AudioLibraryManager.getInstance().getPlaylists(), getActivity()));

        //awake controls
        registerTouchHandlers();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity(); // we can get the reference to activity only then it was created. Not earlier!!

        populateLists();
    }

    private void registerTouchHandlers(){
        //find components
        ListView view = (ListView) activity.findViewById(R.id.list_all_tracks);
        ExpandableListView exAlbumsList = (ExpandableListView) activity.findViewById(R.id.list_all_albums);
        ExpandableListView exArtistsList = (ExpandableListView) activity.findViewById(R.id.list_all_artists);
        ExpandableListView exPlaylistsList = (ExpandableListView) activity.findViewById(R.id.list_custom_playlists);
        ImageView imgRemovePlaylist = (ImageView) activity.findViewById(R.id.imgRemove);
        Button btnAddOkayList = (Button) activity.findViewById(R.id.btn_add_playlist);

        //setup controls
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                playTrack(AudioLibraryManager.getInstance().getLibrary(), pos);
            }
        });

        ///TODO: implement playlist removing mechanism.
//        imgRemovePlaylist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        btnAddOkayList.setOnClickListener(new View.OnClickListener() {
            final String playlist = PromptDialog.showDialog(activity, "test");
            @Override
            public void onClick(View view) {
                AudioLibraryManager.getInstance().addPlaylist(playlist);
            }
        });

        exAlbumsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                playTrack(AudioLibraryManager.getInstance().getAlbums().get(groupPosition), childPosition);
                return false;
            }
        });

        exArtistsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                playTrack(AudioLibraryManager.getInstance().getArtists().get(groupPosition), childPosition);
                return false;
            }

        });

        exPlaylistsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                playTrack(AudioLibraryManager.getInstance().getPlaylists().get(groupPosition), childPosition);
                return false;
            }
        });
    }

    private void playTrack(PlayList playList, int trackIndex){
        playList.setCurrent(trackIndex);
        AudioManager.getInstance().setPlayList(playList);
        AudioManager.getInstance().playTrack();
    }
}