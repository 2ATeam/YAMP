package com.yamp.library;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.yamp.R;

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
        addTab(host, "CUSTOM", R.id.tabCustom);
    }

    private void addTab(TabHost host, String indicator, int resID){
        TabHost.TabSpec spec = host.newTabSpec(indicator.toLowerCase());
        spec.setContent(resID);
        spec.setIndicator(indicator);
        host.addTab(spec);
    }

    private void populateSongsList() {
        SongsListAdapter adapter = new SongsListAdapter();
        ListView view = (ListView) activity.findViewById(R.id.list_all_tracks);
        view.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity(); // we can get the reference to activity only then it was created. Not earlier!!
        populateSongsList();
    }

    public class SongsListAdapter extends ArrayAdapter<AudioFile> {
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
            TextView songName = (TextView) view.findViewById(R.id.song_name);
            songName.setText(track.getName());
            return view;
        }
    }
}
