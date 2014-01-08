package com.yamp.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;

import com.yamp.R;
import com.yamp.fragments.AFXFragment;
import com.yamp.fragments.AudioLibraryFragment;
import com.yamp.fragments.ControlFragment;
import com.yamp.fragments.PlayerFragment;
import com.yamp.fragments.PlaylistEditorFragment;
import com.yamp.fragments.TimelineFragment;
import com.yamp.library.AudioLibraryManager;
import com.yamp.utils.GestureAdapter;
import com.yamp.utils.Logger;


public class PlayerMainActivity extends FragmentActivity {

    private boolean mainFragment = true;

    @Override
    public void onBackPressed() {
        mainFragment=true;
        super.onBackPressed();
    }

    private PlayerFragment playerFragment;
    private AudioLibraryFragment audioLibraryFragment;
    private AFXFragment afxFragment;

    private PlaylistEditorFragment playlistEditorFragment;
    private GestureDetectorCompat gestureDetector;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public AudioLibraryFragment getAudioLibraryFragment() {
        return audioLibraryFragment;
    }

    public PlaylistEditorFragment getPlaylistEditorFragment() {
        return playlistEditorFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVolumeControlStream(android.media.AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_main);

        playerFragment = new PlayerFragment();
        audioLibraryFragment = new AudioLibraryFragment();
        afxFragment = new AFXFragment();
        playlistEditorFragment = new PlaylistEditorFragment();

        if (savedInstanceState != null) return;
        getSupportFragmentManager().beginTransaction().add(R.id.player_fragment, playerFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.timeline_fragment, new TimelineFragment()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.control_fragment, new ControlFragment()).commit();

        initializeGestures();

        AudioLibraryManager.getInstance().setResolver(getContentResolver());
        AudioLibraryManager.getInstance().performFullScan();
        AudioManager.getInstance().setPlayList(AudioLibraryManager.getInstance().getLibrary());
    }

    private void initializeGestures(){
        GestureAdapter gestureAdapter = new GestureAdapter();
        gestureDetector = new GestureDetectorCompat(this, gestureAdapter);
        gestureDetector.setOnDoubleTapListener(gestureAdapter);

        gestureAdapter.setOnFlingListener(new GestureAdapter.FlingListener() {
            @Override
            public void onUpFling() {
                if (mainFragment) {
                    mainFragment = false;
                    replace(afxFragment, new int[]{R.animator.slide_in_bottom, R.animator.slide_out_top, R.animator.slide_in_top, R.animator.slide_out_bottom});
                }
                else{
                    mainFragment = true;
                    getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onDownFling() {
                if (mainFragment) {
                    mainFragment = false;
                    replace(audioLibraryFragment, new int[]{R.animator.slide_in_top,
                                                            R.animator.slide_out_bottom,
                                                            R.animator.slide_in_bottom,
                                                            R.animator.slide_out_top});
                }
                else{
                    mainFragment = true;
                    getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onLeftFling() {
                AudioManager.getInstance().prev();
            }

            @Override
            public void onRightFling() {
                AudioManager.getInstance().next();
            }
        });

        Logger.setGestureAdapter(gestureAdapter);
    }

    /**
     * Replaces main fragment with another specified in newFragment
     * @param newFragment Fragment to be set on the screen
     * @param animation Set of 4 animation resources IDs. May cause Exceptions if wrong values will be passed.
     *                  structure: {new_in,
     *                              old_out,
     *                              old_in,
     *                              new_out}.
     *
     */
    public void replace(Fragment newFragment, int[] animation){
        if (animation.length != 4) return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(animation[0], animation[1], animation[2], animation[3]);
        ft.addToBackStack(null);
        ft.replace(R.id.player_fragment, newFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
