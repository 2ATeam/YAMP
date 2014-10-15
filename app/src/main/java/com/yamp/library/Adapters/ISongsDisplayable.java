package com.yamp.library.Adapters;

import android.view.View;

import com.yamp.library.AudioFile;

/**
 * Created by Lux on 23.12.13.
 */
/// FIXME: temporary solution. Should think about it later.
public interface ISongsDisplayable {
    View getSongView(AudioFile track, View view);
}
