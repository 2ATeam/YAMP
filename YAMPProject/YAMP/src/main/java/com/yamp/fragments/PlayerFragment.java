package com.yamp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.core.YAMPApplication;
import com.yamp.events.TrackLoadedListener;
import com.yamp.library.AlbumArtLoader;
import com.yamp.library.AudioFile;

/**
 * Created by AdYa on 01.12.13.
 */
public class PlayerFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvInfo;
    private ImageView ivCover;


    private void restoreState(){
          updateText(AudioManager.getInstance().getCurrent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragment = inflater.inflate(R.layout.player_fragment, container, false);

        awakeComponents(fragment);
        setCover(AlbumArtLoader.getArtwork(getActivity(), AudioManager.getInstance().getCurrent().getAlbumID()));
        restoreState();

        if (savedInstanceState == null){
            AudioManager.getInstance().setTrackLoadedListener(new TrackLoadedListener() {
                @Override
                public void onNewTrackLoaded(AudioFile track) {
                    updateText(track);
                    setCover(AlbumArtLoader.getArtwork(YAMPApplication.getInstance().getApplicationContext(), track.getAlbumID()));
                }

                @Override
                public void onNextTrackLoaded(AudioFile nextTrack) {
                    ///TODO: add swipe animation to the album cover image
                }

                @Override
                public void onPrevTrackLoaded(AudioFile prevTrack) {
                    ///TODO: add swipe animation to the album cover image
                }
            });
        }

        return fragment;
    }

    private void awakeComponents(View fragment) {
        tvInfo = (TextView) fragment.findViewById(R.id.tvInfo);
        tvTitle = (TextView) fragment.findViewById(R.id.tvTitle);
        ivCover = (ImageView) fragment.findViewById(R.id.ivCover);


    }


    private void updateText(AudioFile track){
        setTitle(track.getName());
        setInfo(String.valueOf(track.getArtist() + " : " + track.getAlbum()));
    }
    public void setTitle(String title) {
        tvTitle.setText(title);
    }
    public void setInfo(String info) {
        tvInfo.setText(info);
    }
    public void setCover(Bitmap bitmap) {
        ivCover.setImageBitmap(bitmap);
    }
}
