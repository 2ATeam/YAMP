package com.yamp.sound;

import android.annotation.TargetApi;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.support.v7.appcompat.R;

import com.yamp.core.YAMPApplication;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.utils.Utilities;

import java.util.ArrayList;

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

        USER_PRESET = new int[getBandsAmount()];

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

    public void setBandLevel(short bandID, int bandLevel){
        if (!Utilities.isWithin(0, getBandsAmount() - 1, bandID)) return;
        int level = (Utilities.clamp(getMinBandLevel(), getMaxBandLevel(), bandLevel) * 100);
        equalizer.setBandLevel(bandID, (short)level);
        USER_PRESET[bandID] = level;
        isUserPreset = true;
    }



    public static int USER_PRESET_ID = -1;
    public static String USER_PRESET_NAME = "Custom";
    private int[] USER_PRESET;
    private boolean isUserPreset = false;

    public int getPresetsAmount(){
        return equalizer.getNumberOfPresets();
    }
    public void setPreset(int presetID){
        if (presetID == USER_PRESET_ID){
            for (short i = 0; i < getBandsAmount(); i++) {
                setBandLevel(i, USER_PRESET[i]);
            }
        }
        else{
            int id = Utilities.clamp(0, getPresetsAmount() - 1, presetID);
            equalizer.usePreset((short)id);
            USER_PRESET = getCurrentBands();
            isUserPreset = false;
        }
    }
    public String getPresetName(int presetID){
        if (Utilities.isWithin(0,getPresetsAmount()-1,presetID))
            return equalizer.getPresetName((short)presetID);
        else if (presetID == USER_PRESET_ID)
            return USER_PRESET_NAME;
        else
            return "Invalid Preset";
    }
    public int getCurrentPreset(){
        if (isUserPreset)
            return USER_PRESET_ID;
        else
            return equalizer.getCurrentPreset();
    }
    public int[] getPreset(int presetID) {
        if (presetID == USER_PRESET_ID){
            return  USER_PRESET;
        }
        else if (Utilities.isWithin(0, getPresetsAmount() - 1, presetID)) {
            int prevPreset = getCurrentPreset();
            setPreset(presetID);
            int[] bands = getCurrentBands();
            setPreset(prevPreset);
            return bands;
        }
        else
            return null;
    }
    public int[] getCurrentBands() {
        int[] res = new int[getBandsAmount()];
        for (short i = 0; i < res.length; i++) {
            res[i] = getCurrentBandLevel(i);
        }
        return res;
    }
}
