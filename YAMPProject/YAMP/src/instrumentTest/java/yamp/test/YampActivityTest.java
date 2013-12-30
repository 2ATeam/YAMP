package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.core.PlayerMainActivity;

/**
 * Created by Lux on 29.12.13.
 */
public class YampActivityTest extends AndroidTestCase{
    public void testConstructor(){
        PlayerMainActivity activity = new PlayerMainActivity();
        assertNotNull(activity);
    }
}
