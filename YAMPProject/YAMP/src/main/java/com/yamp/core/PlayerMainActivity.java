package com.yamp.core;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import com.yamp.R;
import com.yamp.library.AudioLibraryFragment;
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

    private GestureDetectorCompat gestureDetector;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeGestures();

        if (savedInstanceState != null) return;

        getSupportFragmentManager().beginTransaction().add(R.id.player_fragment, playerFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.control_fragment, new ControlFragment()).commit();

        AudioLibraryManager.getInstance().setResolver(getContentResolver());
        AudioLibraryManager.getInstance().performFullScan();


        setContentView(R.layout.activity_main);

        playerFragment = new PlayerFragment();// (PlayerFragment)getSupportFragmentManager().findFragmentById(R.id.player_fragment);
        audioLibraryFragment = new AudioLibraryFragment();

        if (savedInstanceState != null) {
            return;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.player_fragment, playerFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.control_fragment, new ControlFragment()).commit();

        AudioLibraryManager.getInstance().setResolver(getContentResolver());
        AudioLibraryManager.getInstance().scanForAllSongs();
        AudioManager.getInstance().setPlayList(AudioLibraryManager.getInstance().getLibrary());
    }

    private void initializeGestures(){
        GestureAdapter ga = new GestureAdapter();
        gestureDetector = new GestureDetectorCompat(this, ga);
        gestureDetector.setOnDoubleTapListener(ga);

        ga.setOnFlingListener(new GestureAdapter.FlingListener() {
            @Override
            public void onUpFling() {
                if (!mainFragment){
                    mainFragment = true;
                    getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onDownFling() {
                if (mainFragment){
                    mainFragment = false;
                    replace(audioLibraryFragment);
                }
            }

            @Override
            public void onLeftFling() {

            }

            @Override
            public void onRightFling() {

            }
        });

        Logger.setGestureAdapter(ga);
    }

    private void replace(Fragment newFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_bottom, R.animator.slide_in_bottom, R.animator.slide_out_top);
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
