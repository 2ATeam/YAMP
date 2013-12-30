package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.core.ControlFragment;

/**
 * Created by Lux on 30.12.13.
 */
public class ControlFragmentTest extends AndroidTestCase {
    public void testConstructor(){
        ControlFragment f = new ControlFragment();
        assertNotNull(f);
    }
}
