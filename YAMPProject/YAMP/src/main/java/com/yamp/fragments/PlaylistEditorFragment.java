package com.yamp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.library.AudioFile;
import com.yamp.library.AudioLibraryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lux on 29.12.13.
 */
public class PlaylistEditorFragment extends Fragment {

    FragmentActivity activity;
    List<AudioFile> songsToAddToPlaylist;

    public PlaylistEditorFragment() {
        songsToAddToPlaylist = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_library_fragment_playlist_editing, container, false);
        songsToAddToPlaylist.clear();
        return view;
    }

    private void populateContent() {
        //get views
        ListView tracksView = (ListView) activity.findViewById(R.id.lstEdAllSongs);
        tracksView.setAdapter(AudioLibraryManager.getInstance().getPlaylistEditorAdapter());
        TextView playlistName = (TextView) activity.findViewById(R.id.txtEdPlaylistName);
        playlistName.setText(AudioLibraryManager.getInstance().getCurrentlyEditedPlaylist().getName());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        populateContent();
        registerTouchHandlers();
    }

    private void registerTouchHandlers() {
        final Button btnSave = (Button) activity.findViewById(R.id.btnSavePlaylist);
        final ListView list = (ListView) activity.findViewById(R.id.lstEdAllSongs);
        final TextView playlistName = (TextView) activity.findViewById(R.id.txtEdPlaylistName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (AudioFile audioFile : songsToAddToPlaylist) {
                    AudioLibraryManager.getInstance().
                            addSongToPlaylist(AudioLibraryManager.getInstance().
                                    getPlaylistID(playlistName.getText().toString()), audioFile.getID());
                }
                songsToAddToPlaylist.clear();
                AudioLibraryManager.getInstance().scanForPlaylists(); // perform rescanning.
                AudioLibraryManager.getInstance().notifyAllAdapters();
                activity.getSupportFragmentManager().popBackStack();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                AudioFile track = AudioLibraryManager.getInstance().getTrack(pos);
                if (songsToAddToPlaylist.contains(track))
                    songsToAddToPlaylist.remove(track);
                else
                    songsToAddToPlaylist.add(track);
                AudioLibraryManager.getInstance().notifyAllAdapters();
            }
        });
    }
}
