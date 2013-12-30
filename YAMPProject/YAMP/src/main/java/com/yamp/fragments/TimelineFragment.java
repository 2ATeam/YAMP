package com.yamp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.core.YAMPApplication;
import com.yamp.events.PlaybackListener;
import com.yamp.events.TrackLoadedListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.library.AudioFile;
import com.yamp.sound.SoundController;
import com.yamp.utils.Utilities;

/**
 * Created by AdYa on 28.12.13.
 */
public class TimelineFragment extends Fragment{

    private SeekBar sbProgress;

    private TextView tvRemain;
    private TextView tvCurrent;

    private boolean remain = false; // indicates what time should be shown in tvRemain (total or remain)
    private final static int TRACK_PROGRESS_DELAY = 250;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.timeline_fragment, container, false);
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

        AudioManager.getInstance().setPlaybackListener(new PlaybackListener() {
            @Override
            public void onPlayingStarted(boolean causedByUser) {
                sbProgress.setProgress(0);
                sbProgress.setMax(AudioManager.getInstance().getCurrentDuration());
                updateTimers();
                updaterHandler.postDelayed(progressUpdater, TRACK_PROGRESS_DELAY);
            }

            @Override
            public void onPlayingCompleted(boolean causedByUser) {
                updaterHandler.removeCallbacks(progressUpdater);
            }

            @Override
            public void onPlayingPaused(int currentProgress) {
                updaterHandler.removeCallbacks(progressUpdater);
            }

            @Override
            public void onPlayingResumed(int currentProgress) {
                updaterHandler.postDelayed(progressUpdater, TRACK_PROGRESS_DELAY);
            }
        });
    }

    private void awakeComponents(View fragment) {

        sbProgress = (SeekBar) fragment.findViewById(R.id.sbProgress);
        sbProgress.setProgress(0);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private boolean pause; // used for determining state before start seeking.
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
                pause = AudioManager.getInstance().isPaused();
                AudioManager.getInstance().pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updaterHandler.post(progressUpdater); // restore seek bar
                if (!pause)
                    AudioManager.getInstance().play();
            }
        });

        tvRemain = (TextView) fragment.findViewById(R.id.tvTimeRemain);
        tvRemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remain = !remain;
                updateTimers();
            }
        });

        tvCurrent = (TextView) fragment.findViewById(R.id.tvTimeCurrent);
    }

    ///TODO: Setup playing listeners here for managing callbacks

    private Handler updaterHandler = new Handler();
    private Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            sbProgress.setProgress(AudioManager.getInstance().getCurrentProgress());
            updateTimers();
            updaterHandler.postDelayed(progressUpdater, TRACK_PROGRESS_DELAY);
        }
    };

    private void updateTimers(){
        tvCurrent.setText(Utilities.formatTime(YAMPApplication.getSoundController().getProgress()));
        if (remain)
            tvRemain.setText("-" + Utilities.formatTime(YAMPApplication.getSoundController().getDuration() - YAMPApplication.getSoundController().getProgress()));
        else
            tvRemain.setText(Utilities.formatTime(YAMPApplication.getSoundController().getDuration()));
    }
}


