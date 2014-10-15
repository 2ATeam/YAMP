package com.yamp.sound;

import android.media.audiofx.Visualizer;

import com.yamp.core.YAMPApplication;
import com.yamp.events.SoundControllerBoundedListener;

/**
 * Created by AdYa on 24.11.13.
 *
 * Represents audio stream in some nice visualization :D
 * ... 29.12.13 : really? lol..may be next year
 */
public class VisualManager {

    private Visualizer visualizer;

    private static VisualManager instance = new VisualManager();
    public static VisualManager getInstance(){return instance;}

    private VisualManager(){
        if (YAMPApplication.isPlayerReady())
            init(YAMPApplication.getSoundController().getAudioSessionId());
        else
            YAMPApplication.setOnSoundControllerBoundedListener(new SoundControllerBoundedListener() {
                @Override
                public void onSoundControllerBounded(SoundController controller) {
                    init(controller.getAudioSessionId());
                }
            });
    }

    private void init(int audioSessionID){
        visualizer = new Visualizer(audioSessionID);
    }

    public void enable(){
        visualizer.setEnabled(true);
    }
    public void disable(){
        visualizer.setEnabled(false);
    }

    public void setCaptureSize(int size){
        visualizer.setCaptureSize(size);
    }

    public void setDataCaptureListener(Visualizer.OnDataCaptureListener listener, int rate, boolean waveform, boolean fft){
        visualizer.setDataCaptureListener(listener, rate, waveform, fft);
    }

}
