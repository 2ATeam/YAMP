package com.yamp.core;

import com.yamp.library.PlayList;

/**
 * Created by AdYa on 24.11.13.
 *
 * Responses for common operations. (Activity?)
 */
public class AudioManager {
  private boolean looped;
  private boolean shuffle;
  private PlayList trackList; // traget playlist

//  public void play();
//  public void pause();
//  public void stop();
//  public void next();
//  public void prev();
//
// public void setVolume(float volume);

  public void setPlayList(PlayList playlist){
      if (playlist != null && playlist.size() > 0)
        this.trackList = playlist;
  }
}
