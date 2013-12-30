package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.core.YAMPApplication;
import com.yamp.library.AudioLibraryManager;
import com.yamp.sound.SoundController;

/**
 * Created by Lux on 30.12.13.
 */
public class SoundControllerTest extends AndroidTestCase {
    public void testConstructor(){
        SoundController controller = new SoundController();
        assertNotNull(controller);
    }

    public void testDuration(){
        SoundController controller = new SoundController();
        controller.initialize();
        AudioLibraryManager.getInstance().setResolver(YAMPApplication.getInstance().getContentResolver());
        AudioLibraryManager.getInstance().performFullScan();
        controller.setTrack(AudioLibraryManager.getInstance().getTrack(0).getPath());
        int expected = AudioLibraryManager.getInstance().getTrack(0).getDuration();
        int actual = controller.getDuration();
        assertEquals(expected, actual);
    }
}
