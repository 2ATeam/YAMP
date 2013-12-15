package com.yamp.core;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.events.NewTrackLoadedListener;
import com.yamp.library.AudioFile;

/**
 * Created by AdYa on 01.12.13.
 */
public class PlayerFragment extends Fragment {

    private boolean initialized = false;
    private TextView tvTitle;
    private TextView tvInfo;
    private ImageView ivCover;


    private void restoreState(){
            setTitle(AudioManager.getInstance().getCurrent().getName());
            setInfo(AudioManager.getInstance().getCurrent().getFormattedDuration());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragment = inflater.inflate(R.layout.player_fragment, container, false);

        awakeComponents(fragment);
        restoreState();
        return fragment;
    }

    private void awakeComponents(View fragment) {
        tvInfo = (TextView) fragment.findViewById(R.id.tvInfo);
        tvTitle = (TextView) fragment.findViewById(R.id.tvTitle);
        ivCover = (ImageView) fragment.findViewById(R.id.ivCover);

        AudioManager.getInstance().setOnNewTrackLoadedListener(new NewTrackLoadedListener() {
            @Override
            public void onNewTrackLoaded(AudioFile track) {
                setTitle(track.getName());
                setInfo(String.valueOf(track.getFormattedDuration()));
            }
        });
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
