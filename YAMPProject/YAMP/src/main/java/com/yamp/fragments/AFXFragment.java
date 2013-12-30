package com.yamp.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.sound.AFXManager;
import com.yamp.utils.VerticalSeekBar;

/**
 * Created by AdYa on 29.12.13.
 */
public class AFXFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.afx_fragment, container, false);

        createBandFragment(fragment);
        return fragment;
    }

    private void createBandFragment(View parent){
        Context context = getActivity(); /// TODO: if won't work try app context.

        LinearLayout fragment = (LinearLayout)parent.findViewById(R.id.band_fragment);

        final short minEQLevel = AFXManager.getInstance().getMinBandLevel();
        final short maxEQLevel = AFXManager.getInstance().getMaxBandLevel();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (short band = 0; band < AFXManager.getInstance().getBandsAmount(); band++) {
            View bandRow = inflater.inflate(R.layout.band_row, null);

            TextView freqTextView = (TextView)bandRow.findViewById(R.id.tvFrequency);
            freqTextView.setText(AFXManager.getInstance().getCenterFrequency(band) + " Hz");

            TextView minDbTextView = (TextView)bandRow.findViewById(R.id.tvMinLevel);
            minDbTextView.setText(minEQLevel + " dB");

            TextView maxDbTextView = (TextView)bandRow.findViewById(R.id.tvMaxLevel);
            maxDbTextView.setText(maxEQLevel + " dB");

            VerticalSeekBar bar = (VerticalSeekBar)bandRow.findViewById(R.id.sbBand);
            bar.setMax(maxEQLevel + Math.abs(minEQLevel));
            bar.setProgress(AFXManager.getInstance().getCurrentBandLevel(band));

            final short finalBand = band;
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    AFXManager.getInstance().setBandLevel(finalBand, (short) (progress + minEQLevel));
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            fragment.addView(bandRow);
        }
    }
}
