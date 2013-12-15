package com.yamp.core;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.yamp.R;
import com.yamp.events.NewTrackLoadedListener;
import com.yamp.events.PlayingCompletedListener;
import com.yamp.events.PlayingStartedListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.library.AudioFile;
import com.yamp.sound.SoundController;

/**
 * Created by AdYa on 01.12.13.
 */
public class ControlFragment extends Fragment {

    private SeekBar sbProgress;
    private SeekBar sbVolume;
    private Button bPlay;
    private Button bNext;
    private Button bPrev;

    private CheckBox cbLooped;
    private CheckBox cbShuffled;

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
                    sbProgress.setProgress(controller.getProgress());


                }
            });
        }
        return fragment;
    }

    private void restoreState() {
        initialize();

        cbLooped.setChecked(AudioManager.getInstance().isLooped());

        sbVolume.setMax(AudioManager.getInstance().getVolumeMax());
        sbVolume.setProgress(AudioManager.getInstance().getVolume());

        sbProgress.setProgress(0);
        sbProgress.setMax(AudioManager.getInstance().getDuration());

        updaterHandler.post(progressUpdater);
    }


    private void initialize(){
///TODO: Leak of the listeners when recreating fragment !! T_T Terrible bug... May be 'detach listener on destroy' could fix this
        AudioManager.getInstance().setOnNewTrackLoadedListener(new NewTrackLoadedListener() {
            @Override
            public void onNewTrackLoaded(AudioFile track) {
                sbProgress.setProgress(0);
                sbProgress.setMax(AudioManager.getInstance().getDuration());
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
                sbProgress.setProgress(0);
            }
        });

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                if (progress == sbProgress.getMax()){
                    if (AudioManager.getInstance().isPlaying()){
                        updaterHandler.removeCallbacks(progressUpdater);
                        AudioManager.getInstance().stop();
                    }
                }
                AudioManager.getInstance().seekTo(progress);
                ///TODO: Change 'real-time' scrolling to end-tracking scrolling... ??
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbVolume.setMax(AudioManager.getInstance().getVolumeMax());
        sbVolume.setProgress(AudioManager.getInstance().getVolume());

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

    private void awakeComponents(View fragment) {
        bPlay = (Button) fragment.findViewById(R.id.bPlay);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playHandler();
            }
        });

     //   bPlay.setEnabled(false);

        bNext = (Button) fragment.findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextHandler();
            }
        });

       // bNext.setEnabled(false);

        bPrev = (Button) fragment.findViewById(R.id.bPrev);
        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevHandler();
            }
        });

        //bPrev.setEnabled(false);

        cbLooped = (CheckBox) fragment.findViewById(R.id.cbLoop);
        cbLooped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                loopedHandler(b);
            }
        });

        sbProgress = (SeekBar) fragment.findViewById(R.id.sbProgress);
        sbProgress.setProgress(0);

        sbVolume = (SeekBar)fragment.findViewById(R.id.sbVolume);

    }


    private Handler updaterHandler = new Handler();
    private Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            sbProgress.setProgress(YAMPApplication.getSoundController().getProgress());
            updaterHandler.postDelayed(progressUpdater, TRACK_PROGRESS_DELAY);
        }
    };

    private void loopedHandler(boolean looped) {
        AudioManager.getInstance().setLooping(looped); /// TODO: Replace simple boolean with LOOP_MODE enum.
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

}
