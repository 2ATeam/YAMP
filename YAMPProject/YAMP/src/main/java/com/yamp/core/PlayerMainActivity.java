package com.yamp.core;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import com.yamp.R;
import com.yamp.library.AudioLibraryFragment;
import com.yamp.library.AudioLibraryManager;


public class PlayerMainActivity extends FragmentActivity {

    private Button bSwitch;
    private boolean mainFragment = true;

    private PlayerFragment playerFragment;
    private AudioLibraryFragment audioLibraryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioLibraryManager.getInstance().setResolver(getContentResolver());
        AudioLibraryManager.getInstance().scanForAllSongs();
        AudioLibraryManager.getInstance().scanForAlbums();
        AudioLibraryManager.getInstance().scanForArtists();

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

        bSwitch = (Button)findViewById(R.id.bSwitch);
        bSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mainFragment){
                    mainFragment = false;
                    replace(audioLibraryFragment);
                }
                else{
                    mainFragment = true;
                    getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private void replace(Fragment newFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
