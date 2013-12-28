package com.yamp.core;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.events.NewTrackLoadedListener;
import com.yamp.events.PlayingCompletedListener;
import com.yamp.events.PlayingStartedListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.library.AudioFile;
import com.yamp.sound.SoundController;
import com.yamp.utils.LoopButton;
import com.yamp.utils.Utilities;

/**
 * Created by AdYa on 01.12.13.
 */
public class ControlFragment extends Fragment {

    private SeekBar sbProgress;
    private SeekBar sbVolume;
    private Button bPlay;
    private Button bNext;
    private Button bPrev;

    private TextView tvRemain; ///TODO: make it clickable
    private TextView tvCurrent;

    private LoopButton lbLooped;
    private CheckBox cbShuffled; ///TODO: change appearance for this checkbox

    private final static int TRACK_PROGRESS_DELAY = 250;

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
        updaterHandler.removeCallbacks(progressUpdater); // make sure that handler is clear.
        lbLooped.setState(AudioManager.getInstance().getLoopMode());

        sbVolume.setMax(AudioManager.getInstance().getVolumeMax());
        sbVolume.setProgress(AudioManager.getInstance().getVolume());

        sbProgress.setProgress(0);
        sbProgress.setMax(AudioManager.getInstance().getCurrentDuration());

        updateTimers();

        updaterHandler.post(progressUpdater);
    }


    private void initialize(){
        AudioManager.getInstance().setOnNewTrackLoadedListener(new NewTrackLoadedListener() {
            @Override
            public void onNewTrackLoaded(AudioFile track) {
                sbProgress.setProgress(0);
                sbProgress.setMax(AudioManager.getInstance().getCurrentDuration());
            }
        });

        AudioManager.getInstance().setOnPlayingStartedListener(new PlayingStartedListener() {
            @Override
            public void onPlayingStarted() {
                updaterHandler.post(progressUpdater);
            }
        });

        AudioManager.getInstance().setOnPlayingCompletedListener(new PlayingCompletedListener() {
            @Override
            public void onPlayingCompleted() {
                updaterHandler.removeCallbacks(progressUpdater);
            }
        });

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

        sbProgress = (SeekBar) fragment.findViewById(R.id.sbProgress);
        sbProgress.setProgress(0);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;



                if (progress == sbProgress.getMax()){
                    if (AudioManager.getInstance().isPlaying()){
                        AudioManager.getInstance().stop();
                    }
                }

                AudioManager.getInstance().seekTo(progress);
                tvCurrent.setText(Utilities.formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updaterHandler.removeCallbacks(progressUpdater); // pause seek bar
                AudioManager.getInstance().pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updaterHandler.post(progressUpdater); // restore seek bar
                AudioManager.getInstance().play();
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

        tvRemain = (TextView) fragment.findViewById(R.id.tvTimeRemain);
        tvCurrent = (TextView) fragment.findViewById(R.id.tvTimeCurrent);
    }


    private Handler updaterHandler = new Handler();
    private Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            sbProgress.setProgress(YAMPApplication.getSoundController().getProgress());
            updateTimers();
            updaterHandler.postDelayed(progressUpdater, TRACK_PROGRESS_DELAY);
        }
    };

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
            updaterHandler.removeCallbacks(progressUpdater);
        } else {
            AudioManager.getInstance().play();
            updaterHandler.post(progressUpdater);
        }
    }

    private void updateTimers(){
        tvCurrent.setText(Utilities.formatTime(YAMPApplication.getSoundController().getProgress()));
        tvRemain.setText(Utilities.formatTime(YAMPApplication.getSoundController().getDuration()));
    }
}
