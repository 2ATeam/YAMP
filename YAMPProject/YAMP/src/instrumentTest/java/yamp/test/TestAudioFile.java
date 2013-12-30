package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.library.AudioFile;

/**
 * Created by Lux on 30.12.13.
 */
public class TestAudioFile extends AndroidTestCase {
    public void testConstructor() {
        AudioFile file = new AudioFile(0, "lol", "art", "alb", 10000, "somewhere");
        assertNotNull(file);
    }

    public void testName(){
        AudioFile file = new AudioFile(0, "lol", "art", "alb", 10000, "somewhere");
        String expected = "lol";
        String actual = file.getName();
        assertEquals(expected, actual);
    }

    public void testAlbumName(){
        AudioFile file = new AudioFile(0, "lol", "art", "alb", 10000, "somewhere");
        String expected = "alb";
        String actual = file.getAlbum();
        assertEquals(expected, actual);
    }

    public void testArtistName(){
        AudioFile file = new AudioFile(0, "lol", "art", "alb", 10000, "somewhere");
        String expected = "art";
        String actual = file.getArtist();
        assertEquals(expected, actual);
    }

    public void testPath(){
        AudioFile file = new AudioFile(0, "lol", "art", "alb", 10000, "somewhere");
        String expected = "somewhere";
        String actual = file.getPath();
        assertEquals(expected, actual);
    }
}
