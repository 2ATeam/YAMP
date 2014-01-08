package com.yamp.fragments;

import android.content.Context;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yamp.R;
import com.yamp.sound.AFXManager;
import com.yamp.sound.VisualManager;
import com.yamp.utils.VerticalSeekBar;

import java.util.ArrayList;

/**
 * Created by AdYa on 29.12.13.
 */
public class AFXFragment extends Fragment {

    private ArrayList<VerticalSeekBar> bandBars = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.afx_fragment, container, false);

        createBandFragment(fragment);
        createVisualizer(fragment);
        return fragment;
    }

    ///TODO: Unstable visualizer
    private void createVisualizer(View parent){
        FrameLayout v = (FrameLayout)parent.findViewById(R.id.visualizer_fragment);
        final VisualizerView mVisualizerView = new VisualizerView(getActivity());
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        v.addView(mVisualizerView);

        VisualManager.getInstance().setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        VisualManager.getInstance().setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }
    private void createBandFragment(View parent){
        bandBars.clear();
        Context context = getActivity();

        LinearLayout fragment = (LinearLayout)parent.findViewById(R.id.band_fragment);

        final short minEQLevel = AFXManager.getInstance().getMinBandLevel();
        final short maxEQLevel = AFXManager.getInstance().getMaxBandLevel();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 10, 0);

        for (short band = 0; band < AFXManager.getInstance().getBandsAmount(); band++) {
            View bandRow = inflater.inflate(R.layout.band_row, null);
            bandRow.setLayoutParams(lp);

            TextView freqTextView = (TextView)bandRow.findViewById(R.id.tvFrequency);
            freqTextView.setText(AFXManager.getInstance().getCenterFrequency(band) + " Hz");

            TextView minDbTextView = (TextView)bandRow.findViewById(R.id.tvMinLevel);
            minDbTextView.setText(minEQLevel + " dB");

            TextView maxDbTextView = (TextView)bandRow.findViewById(R.id.tvMaxLevel);
            maxDbTextView.setText(maxEQLevel + " dB");

            VerticalSeekBar bar = (VerticalSeekBar)bandRow.findViewById(R.id.sbBand);
            bar.setMax(maxEQLevel + Math.abs(minEQLevel));
            bar.setProgress(AFXManager.getInstance().getCurrentBandLevel(band) + maxEQLevel);

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
            bandBars.add(bar);


            fragment.addView(bandRow);
        }
        applyPreset(AFXManager.USER_PRESET_ID);
        // create preset buttons

        LinearLayout presetsLayout = new LinearLayout(fragment.getContext());
        presetsLayout.setOrientation(LinearLayout.VERTICAL);
        fragment.addView(presetsLayout);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,2,0,2);
        presetsLayout.setLayoutParams(lp);
        for (int i = 0; i < AFXManager.getInstance().getPresetsAmount(); i++) {
            final int presetID = i;
            Button bPreset = new Button(fragment.getContext());
            bPreset.setLayoutParams(lp);
            bPreset.setText(AFXManager.getInstance().getPresetName(presetID));
            bPreset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    applyPreset(presetID);
                    AFXManager.getInstance().setPreset(presetID);
                }
            });
            presetsLayout.addView(bPreset);
        }
    }

    private void applyPreset(int presetID) {
        int[] bands = AFXManager.getInstance().getPreset(presetID);
        if (bands == null) return;

        for (int i = 0; i < bands.length; i++) {
            bandBars.get(i).setProgress(bands[i] + AFXManager.getInstance().getMaxBandLevel());
        }
    }
}
