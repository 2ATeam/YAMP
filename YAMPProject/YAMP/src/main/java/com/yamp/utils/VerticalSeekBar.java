package com.yamp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import java.util.ArrayList;

/**
 * Created by AdYa on 29.12.13.
 *
 * All what this does is just rotate existing SeekBar at -90 degrees.
 */
public class VerticalSeekBar extends SeekBar {

    private ArrayList<OnSeekBarChangeListener> seekBarChangeListeners = new ArrayList<>();
    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener){
        this.seekBarChangeListeners.add(mListener);
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        for (OnSeekBarChangeListener listener : seekBarChangeListeners) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    listener.onStartTrackingTouch(this);
                    break;
                case MotionEvent.ACTION_MOVE:
                    setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                    onSizeChanged(getWidth(), getHeight(), 0, 0);
                    listener.onProgressChanged(this, getMax() - (int) (getMax() * event.getY() / getHeight()), true);
                    break;
                case MotionEvent.ACTION_UP:
                    listener.onStopTrackingTouch(this);
                    break;

                case MotionEvent.ACTION_CANCEL:
                    break;
            }
        }
        invalidate();
        return true;
    }
}
