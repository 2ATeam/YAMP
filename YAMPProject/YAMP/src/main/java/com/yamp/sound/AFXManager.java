package com.yamp.sound;

import android.annotation.TargetApi;
import android.media.AudioManager;
import android.media.audiofx.*;

import com.yamp.core.YAMPApplication;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.utils.Utilities;

/**
 * Created by AdYa on 24.11.13.
 *
 * Tunes audio stream :)
 */
@TargetApi(9)
public class AFXManager {

    private Equalizer equalizer;
    private Virtualizer virtualizer;
    private BassBoost bassBoost;
    private PresetReverb reverb;

    private static AFXManager instance = new AFXManager();

    public static AFXManager getInstance(){return instance;}

    private AFXManager(){
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
        equalizer = new Equalizer(0, audioSessionID);
        equalizer.setEnabled(true);

       /* virtualizer = new Virtualizer(0, audioSessionID);
        bassBoost = new BassBoost(0, audioSessionID);
        reverb = new PresetReverb(0, audioSessionID);

        virtualizer.setEnabled(true);
        bassBoost.setEnabled(true);*/
    }

    public short getBandsAmount() {return equalizer.getNumberOfBands();}
    public short getMinBandLevel(){ return (short)(equalizer.getBandLevelRange()[0] / 100);}
    public short getMaxBandLevel(){ return (short)(equalizer.getBandLevelRange()[1] / 100);}

    /**
     * Retrieves level of the specified band
     * @param bandID Band id whose level was requested
     * @return Level in dB
     */
    public int getCurrentBandLevel(short bandID){
        if (!Utilities.isWithin(0, getBandsAmount() - 1, bandID)) return 0;
        return equalizer.getBandLevel(bandID) / 100;
    }

    /**
     * Retrieves frequency of the specified band
     * @param bandID Band id whose frequency was requested
     * @return Frequency in Hz
     */
    public int getCenterFrequency(short bandID){
        if (!Utilities.isWithin(0, getBandsAmount() - 1, bandID)) return 0;
        return equalizer.getCenterFreq(bandID) / 1000;
    }

    public void setBandLevel(short bandID, int level){
        if (!Utilities.isWithin(0, getBandsAmount() - 1, bandID)) return;
        equalizer.setBandLevel(bandID, (short)(Utilities.clamp(getMinBandLevel(), getMaxBandLevel(), level) * 100));
    }
}
