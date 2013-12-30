package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.core.AudioManager;

/**
 * Created by Lux on 30.12.13.
 */
public class AudioManagerTest extends AndroidTestCase {
    public void testConstructor(){
        assertNotNull(AudioManager.getInstance());
    }
}
