package com.yamp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.library.AudioLibraryManager;
import com.yamp.library.PlayList;

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

        //set data adapters
        AudioLibraryManager.getInstance().initDataAdapters(activity);
        allTracksView.setAdapter(AudioLibraryManager.getInstance().getSongsListAdapter());
        albumsListView.setAdapter(AudioLibraryManager.getInstance().getAlbumsListAdapter());
        artistListView.setAdapter(AudioLibraryManager.getInstance().getArtistsListAdapter());
        playlistsListView.setAdapter(AudioLibraryManager.getInstance().getPlaylistsListAdapter());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity(); // we can get the reference to activity only then it was created. Not earlier!!
        populateLists();

        //awake controls
        registerTouchHandlers();
    }

    private void registerTouchHandlers() {
        //find components
        ListView view = (ListView) activity.findViewById(R.id.list_all_tracks);
        ExpandableListView exAlbumsList = (ExpandableListView) activity.findViewById(R.id.list_all_albums);
        ExpandableListView exArtistsList = (ExpandableListView) activity.findViewById(R.id.list_all_artists);
        ExpandableListView exPlaylistsList = (ExpandableListView) activity.findViewById(R.id.list_custom_playlists);
        Button btnAddPlaylist = (Button) activity.findViewById(R.id.btn_add_playlist);

        //setup controls
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                playTrack(AudioLibraryManager.getInstance().getLibrary(), pos);
            }
        });

        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText txt = new EditText(activity);
                new AlertDialog.Builder(activity) /// TODO: should be refactored later. (put this code into separate class)
                        .setTitle("Enter Playlist Name:")
                        .setView(txt)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String reply = txt.getText().toString().trim();
                                if (reply.length() <= 0)
                                    reply = "empty";
                                if (!AudioLibraryManager.getInstance().addPlaylist(reply)) {
                                    Toast.makeText(activity, "Same Playlist names are not allowed.", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }).show();
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