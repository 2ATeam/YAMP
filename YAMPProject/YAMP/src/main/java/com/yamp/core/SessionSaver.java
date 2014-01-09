package com.yamp.core;

import android.content.SharedPreferences;

import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.library.AudioLibrary;
import com.yamp.library.AudioLibraryManager;
import com.yamp.sound.SoundController;

/**
 * Created by AdYa on 09.01.14.
 */
public class SessionSaver {

    public final static String PREF_NAME = "YAMPSession";

    private final static String VOLUME = "volume";
    private final static String CURRENT_TRACK = "track";
    private final static String CURRENT_PLAYLIST = "playlist";
    private final static String LOOP_MODE = "loop";
    private final static String SHUFFLED = "shuffle";


    public static void save(){
        SharedPreferences s = YAMPApplication.getInstance().getSharedPreferences( PREF_NAME, YAMPApplication.getInstance().MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();
        e.putInt(VOLUME, AudioManager.getInstance().getVolume());
        e.putInt(LOOP_MODE, AudioManager.getInstance().getLoopMode());
        e.putBoolean(SHUFFLED, AudioManager.getInstance().isShuffled());
        e.putLong(CURRENT_PLAYLIST, AudioManager.getInstance().getCurrentPlayList().getID());
        e.putInt(CURRENT_TRACK, AudioManager.getInstance().getCurrentPlayList().getCurrent());
        e.apply();
    }

    public static void load(){

        if (YAMPApplication.isPlayerReady())
            safeLoad();
        else
            YAMPApplication.setOnSoundControllerBoundedListener(new SoundControllerBoundedListener() {
                @Override
                public void onSoundControllerBounded(SoundController controller) {
                    safeLoad();
                }
            });
    }

    private static void safeLoad(){
        SharedPreferences s = YAMPApplication.getInstance().getSharedPreferences( PREF_NAME, YAMPApplication.getInstance().MODE_PRIVATE);

        AudioManager.getInstance().setVolume(s.getInt(VOLUME, AudioManager.getInstance().getVolume()));
        AudioManager.getInstance().setLoopMode(s.getInt(LOOP_MODE, 0));
        AudioManager.getInstance().setShuffle(s.getBoolean(SHUFFLED, false));
        AudioManager.getInstance().setPlayList(AudioLibraryManager.getInstance().getPlaylistByID(s.getLong(CURRENT_PLAYLIST, AudioLibraryManager.getInstance().getLibrary().getID())));
        AudioManager.getInstance().getCurrentPlayList().setCurrent(s.getInt(CURRENT_TRACK, 0));
    }
}
