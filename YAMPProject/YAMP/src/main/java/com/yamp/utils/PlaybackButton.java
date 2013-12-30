package com.yamp.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.yamp.R;

/**
 * Created by AdYa on 23.12.13.
 */
public class PlaybackButton extends Button {


    // a little bit scary code (not enum because of iterating throw this states is looped)
    public static final int STATE_PAUSED = 0;
    public static final int STATE_PLAYING = 1;

    private static final int STATES_AMOUNT = 2;
    private static final int[] STATE_PAUSED_SET = {R.attr.state_paused};
    private static final int[] STATE_PLAYING_SET = {R.attr.state_playing};

    private int state = 0;

    public PlaybackButton(Context context) {
        super(context);
    }

    public PlaybackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaybackButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + STATES_AMOUNT);

        switch (state) {
            case STATE_PAUSED:
                mergeDrawableStates(drawableState, STATE_PLAYING_SET);
                break;
            case STATE_PLAYING:
                mergeDrawableStates(drawableState, STATE_PAUSED_SET);
                break;
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        switchState();
        return super.performClick();
    }

    private void switchState() {
        state = (state + 1) % STATES_AMOUNT;
    }

    public int getState() {
        return state;
    }

    public void setState(int state){
        this.state = Utilities.clamp(STATE_PAUSED, STATES_AMOUNT - 1, state);
    }
}
