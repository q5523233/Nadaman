package com.sm.nadaman.common.helper;

import android.content.Context;

import com.sm.nadaman.common.widget.ecg.WaveTView;
import com.sm.nadaman.common.widget.ecg.WaveViewConfig;


public class DataHelpeFactory {
    public static Ecg12ViewDataHelper create( Context context, WaveViewConfig config) {
        return new EcgViewHelper(context, config);
    }

    public static Ecg12ViewDataHelper create(WaveTView[] waveTView, WaveViewConfig config) {
        return new EcgViewHelper(waveTView, config);
    }
}
