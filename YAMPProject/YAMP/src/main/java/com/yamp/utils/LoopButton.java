package com.yamp.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.yamp.R;

/**
 * Created by AdYa on 23.12.13.
 */
public class LoopButton extends CompoundButton {


    // a little bit scary code (not enum because of iterating throw this states is looped)
    public static final int STATE_NONE = 0;
    public static final int STATE_SINGLE = 1;
    public static final int STATE_ALL = 2;

    private static final int STATES_AMOUNT = 3;
    private static final int[] STATE_LOOPING_NONE_SET = {R.attr.state_looping_none};
    private static final int[] STATE_LOOPING_SINGLE_SET = {R.attr.state_looping_single};
    private static final int[] STATE_LOOPING_ALL_SET = {R.attr.state_looping_all};

    private int state = 0;

    public int getState() {
        return state;
    }

    public LoopButton(Context context) {
        super(context);
    }

    public LoopButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public LoopButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + STATES_AMOUNT);

        switch (state) {
            case STATE_NONE:
                mergeDrawableStates(drawableState, STATE_LOOPING_NONE_SET);
                break;
            case STATE_SINGLE:
                mergeDrawableStates(drawableState, STATE_LOOPING_SINGLE_SET);
                break;
            case STATE_ALL:
                mergeDrawableStates(drawableState, STATE_LOOPING_ALL_SET);
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
}
