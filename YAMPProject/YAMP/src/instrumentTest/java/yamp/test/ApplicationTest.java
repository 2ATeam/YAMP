package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.core.YAMPApplication;

/**
 * Created by Lux on 30.12.13.
 */
public class ApplicationTest extends AndroidTestCase {
    public void testConstructor(){
        assertNotNull(YAMPApplication.getInstance());
    }
}
