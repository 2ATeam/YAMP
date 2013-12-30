package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.core.YAMPApplication;
import com.yamp.library.AudioLibraryManager;


/**
 * Created by Lux on 30.12.13.
 */
public class AudioLibraryManagerTest extends AndroidTestCase{
    public void testConstructor(){
        AudioLibraryManager instance = AudioLibraryManager.getInstance();
        assertNotNull(instance);
    }

    public void testPlaylistInsertion(){
        AudioLibraryManager instance = AudioLibraryManager.getInstance();
        instance.setResolver(YAMPApplication.getInstance().getContentResolver());
        assertTrue(instance.addPlaylist("test"));
    }

    public void testEqualPlaylistAddition(){
        AudioLibraryManager instance = AudioLibraryManager.getInstance();
        instance.setResolver(YAMPApplication.getInstance().getContentResolver());
        instance.addPlaylist("lol");
        instance.addPlaylist("lol");
        int expected = 1;
        instance.scanForPlaylists();
        int actual = instance.getPlaylists().size();
        assertEquals(expected, actual);
    }

    public void testUncreatedTrack(){
        AudioLibraryManager instance = AudioLibraryManager.getInstance();
        instance.setResolver(YAMPApplication.getInstance().getContentResolver());
        assertNotNull(instance.getTrack(3));
    }

    public void testPlaylistExistence(){
        AudioLibraryManager instance = AudioLibraryManager.getInstance();
        instance.setResolver(YAMPApplication.getInstance().getContentResolver());
        instance.addPlaylist("test");
        instance.performFullScan();
        assertTrue(instance.isPlaylistExists("test"));
    }
}
