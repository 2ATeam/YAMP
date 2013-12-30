package yamp.test;

import android.test.AndroidTestCase;

import com.yamp.library.AudioFile;
import com.yamp.library.AudioLibrary;
import com.yamp.library.PlayList;

/**
 * Created by Lux on 30.12.13.
 */
public class AudioLibraryTest extends AndroidTestCase{
    public void testConstructor(){
        AudioLibrary library = new AudioLibrary();
        assertNotNull(library);
    }

    public void testPlaylistsCreation(){
        AudioLibrary library = new AudioLibrary();
        assertNotNull(library.getPlayLists());
    }

    public void testPlaylistsInsertion(){
        AudioLibrary library = new AudioLibrary();
        library.getPlayLists().add(new PlayList("test", 0));
        assertNotNull(library.getPlayLists().get(0));
    }

    public void testLibraryWipe(){
        AudioLibrary library = new AudioLibrary();
        library.clearAll();
        assertEquals(library.getTracks().size(), 0);
    }

    public void testInsertTrack(){
        AudioLibrary library = new AudioLibrary();
        library.addTrack(new AudioFile("somewhere"));
        assertEquals(1, library.size());
    }
}
