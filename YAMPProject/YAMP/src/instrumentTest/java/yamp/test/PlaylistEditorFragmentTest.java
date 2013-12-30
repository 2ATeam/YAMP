package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.fragments.PlaylistEditorFragment;

/**
 * Created by Lux on 30.12.13.
 */
public class PlaylistEditorFragmentTest extends AndroidTestCase {
    public void testConstructor(){
        PlaylistEditorFragment fragment = new PlaylistEditorFragment();
        assertNotNull(fragment);
    }
}
