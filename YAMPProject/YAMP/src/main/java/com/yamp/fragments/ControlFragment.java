package com.yamp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.core.YAMPApplication;
import com.yamp.events.PlaybackListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.sound.SoundController;
import com.yamp.utils.LoopButton;

/**
 * Created by AdYa on 01.12.13.
 */
public class ControlFragment extends Fragment {

    private SeekBar sbVolume;
    private CheckBox bPlay; // Checked - playing; not checked - paused
    private Button bNext;
    private Button bPrev;

    private LoopButton lbLooped;
    private CheckBox cbShuffled;
    private CheckBox cbVolume;

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
                    AudioManager.getInstance().setPlaybackListener(new PlaybackListener() {
                        @Override
                        public void onPlayingStarted(boolean causedByUser) {
                            bPlay.setChecked(AudioManager.getInstance().isPlaying());
                        }

                        @Override
                        public void onPlayingCompleted(boolean causedByUser) {
                            bPlay.setChecked(AudioManager.getInstance().isPlaying());
                        }

                        @Override
                        public void onPlayingPaused(int currentProgress) {
                            bPlay.setChecked(AudioManager.getInstance().isPlaying());
                        }

                        @Override
                        public void onPlayingResumed(int currentProgress) {
                            bPlay.setChecked(AudioManager.getInstance().isPlaying());
                        }
                    });
                }
            });
        }



        return fragment;
    }

    private void restoreState() {
        initialize();
        lbLooped.setState(AudioManager.getInstance().getLoopMode());
        cbVolume.setChecked(AudioManager.getInstance().getVolume() != 0);
        bPlay.setChecked(AudioManager.getInstance().isPlaying());
    }


    private void initialize(){
        sbVolume.setMax(AudioManager.getInstance().getVolumeMax());
        sbVolume.setProgress(AudioManager.getInstance().getVolume());
    }

    private void awakeComponents(View fragment) {
        bPlay = (CheckBox) fragment.findViewById(R.id.bPlay);
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

                cbVolume.setChecked(progress!=0);
                AudioManager.getInstance().setVolume(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cbVolume = (CheckBox)fragment.findViewById(R.id.cbVolume);
        cbVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int lastVolume;
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    AudioManager.getInstance().setVolume(lastVolume);
                    sbVolume.setProgress(lastVolume);
                }
                else{
                    lastVolume = AudioManager.getInstance().getVolume();
                    AudioManager.getInstance().setVolume(0);
                    sbVolume.setProgress(0);
                }
            }
        });

        cbShuffled = (CheckBox)fragment.findViewById(R.id.cbShuffle);
        cbShuffled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    AudioManager.getInstance().setShuffle(isChecked);
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
