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
 * Created by AdYa on 28.12.13.
 */
public class TimiLineFragment extends Fragment{

    private SeekBar sbProgress;

    private TextView tvRemain; ///TODO: make it clickable
    private TextView tvCurrent;

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
                }
            });
        }
        return fragment;
    }

    private void restoreState() {
        updaterHandler.removeCallbacks(progressUpdater); // make sure that handler is clear.

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
    }

    private void awakeComponents(View fragment) {

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


