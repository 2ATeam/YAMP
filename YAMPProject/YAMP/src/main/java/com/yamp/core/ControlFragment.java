package com.yamp.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.yamp.R;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.sound.SoundController;
import com.yamp.utils.LoopButton;

/**
 * Created by AdYa on 01.12.13.
 */
public class ControlFragment extends Fragment {

    private SeekBar sbVolume;
    private Button bPlay;
    private Button bNext;
    private Button bPrev;

    private LoopButton lbLooped;
    private CheckBox cbShuffled; ///TODO: change appearance for this checkbox

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.control_panel_fragment, container, false);
        awakeComponents(fragment);
        if (YAMPApplication.getInstance().isPlayerReady())
            restoreState();
        else{
            YAMPApplication.setOnSoundControllerBoundedListener(new SoundControllerBoundedListener() {
                @Override
                public void onSoundControllerBounded(SoundController controller) {
                    initialize();
                    controller.setVolume(sbVolume.getProgress());
                }
            });
        }



        return fragment;
    }

    private void restoreState() {
        lbLooped.setState(AudioManager.getInstance().getLoopMode());

        sbVolume.setMax(AudioManager.getInstance().getVolumeMax());
        sbVolume.setProgress(AudioManager.getInstance().getVolume());
    }


    private void initialize(){
        sbVolume.setMax(AudioManager.getInstance().getVolumeMax());
        sbVolume.setProgress(AudioManager.getInstance().getVolume());
    }

    private void awakeComponents(View fragment) {
        bPlay = (Button) fragment.findViewById(R.id.bPlay);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playHandler();
            }
        });


        bNext = (Button) fragment.findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextHandler();
            }
        });


        bPrev = (Button) fragment.findViewById(R.id.bPrev);
        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevHandler();
            }
        });


        lbLooped = (LoopButton) fragment.findViewById(R.id.cbLoop);
        lbLooped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopedHandler(lbLooped.getState());
            }
        });

        sbVolume = (SeekBar)fragment.findViewById(R.id.sbVolume);
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser) return;
                AudioManager.getInstance().setVolume(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void loopedHandler(int loopMode) {
        AudioManager.getInstance().setLoopMode(loopMode); /// TODO: Replace simple boolean with LOOP_MODE enum.
    }

    private void prevHandler() {
        AudioManager.getInstance().prev();
    }

    private void nextHandler() {
        AudioManager.getInstance().next();
    }

    private void playHandler() {
        if (AudioManager.getInstance().isPlaying()){
            AudioManager.getInstance().pause();
        } else {
            AudioManager.getInstance().play();
        }
    }
}
