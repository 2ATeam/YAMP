package com.yamp.core;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yamp.R;
import com.yamp.sound.SoundController;

/**
 * Created by AdYa on 01.12.13.
 */
public class ControlFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.control_panel_fragment, container, false);
        awakeComponents(fragment);
        return fragment;
    }


    public void awakeComponents(View fragment){
        Button bPlay = (Button)fragment.findViewById(R.id.bPlay);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playHandler();
            }
        });


        Button bNext = (Button)fragment.findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextHandler();
            }
        });
    }

    private void nextHandler() {

    }

    private void playHandler() {
        SoundController c = YAMPApplication.getSoundController();

        if (c.isPlaying())
            c.pause();
        else
            c.play();
    }

}
