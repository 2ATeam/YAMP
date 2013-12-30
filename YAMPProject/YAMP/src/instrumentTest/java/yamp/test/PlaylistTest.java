package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.library.AudioFile;
import com.yamp.library.PlayList;

/**
 * Created by Lux on 30.12.13.
 */
public class PlaylistTest extends AndroidTestCase {
    public void testConstructor() {
        PlayList playList = new PlayList();
        assertNotNull(playList);
    }

    public void testAddTrack(){
        PlayList playList = new PlayList();
        playList.addTrack(new AudioFile("somewhere"));
        assertNotNull(playList.getTrack(0));
    }

    public void testNextTrackSwitch(){
        PlayList playList = new PlayList();
        playList.addTrack(new AudioFile("prev"));
        playList.addTrack(new AudioFile("next"));
        playList.setCurrent(0);

        String expected = "next";
        String actual = playList.nextTrack().getPath();
        assertEquals(expected, actual);
    }

    public void testPrevTrackSwitch(){
        PlayList playList = new PlayList();
        playList.addTrack(new AudioFile("prev"));
        playList.addTrack(new AudioFile("next"));
        playList.setCurrent(1);

        String expected = "prev";
        String actual = playList.prevTrack().getPath();
        assertEquals(expected, actual);
    }

    public void testSize(){
        PlayList playList = new PlayList();
        playList.addTrack(new AudioFile("prev"));
        playList.addTrack(new AudioFile("next"));
        int expected = 2;
        int actual = playList.size();
        assertEquals(expected, actual);
    }
}
