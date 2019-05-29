package com.example.imagefilters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.seekBarBrightness)
    private SeekBar seekBarBrightness;
    @BindView(R.id.seekBarContrast)
    private SeekBar seekBarContrast;
    @BindView(R.id.seekBarSaturation)
    private SeekBar seekBarSaturation;

    private EditImageFragmentListener listener;

    public void setListener(EditImageFragmentListener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        ButterKnife.bind(view);

        seekBarBrightness.setMax(200);
        seekBarBrightness.setProgress(100);

        seekBarContrast.setMax(20);
        seekBarContrast.setProgress(0);

        seekBarSaturation.setMax(30);
        seekBarSaturation.setProgress(10);

        seekBarBrightness.setOnSeekBarChangeListener(this);
        seekBarContrast.setOnSeekBarChangeListener(this);
        seekBarSaturation.setOnSeekBarChangeListener(this);

        return view;
    }

    public void resetControls() {
        seekBarBrightness.setProgress(100);
        seekBarContrast.setProgress(0);
        seekBarSaturation.setProgress(10);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener == null) return;
        switch (seekBar.getId()) {
            case R.id.seekBarBrightness:
                if (listener != null)
                    listener.onBrightnessChanged(progress - 100);
                break;
            case R.id.seekBarContrast:
                progress += 10;
                float flotContrast = .10f * progress;
                if (listener != null)
                    listener.onContrastChanged(flotContrast);
                break;
            case R.id.seekBarSaturation:
                float flotSaturation = .10f * progress;
                if (listener != null)
                    listener.onContrastChanged(flotSaturation);
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (listener != null)
            listener.editStarted();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (listener != null)
            listener.editCompleted();
    }

    public interface EditImageFragmentListener {
        void onBrightnessChanged(int brightness);
        void onContrastChanged(float contrast);
        void onSaturationChanged(float saturation);
        void editStarted();
        void editCompleted();
    }
}
