package com.sm.nadaman.common.helper;


import com.sm.nadaman.common.widget.ecg.WaveTView;
import com.sm.nadaman.common.widget.ecg.WaveViewConfig;

import java.util.List;

public interface Ecg12ViewDataHelper {
    void  addData(short[] data);
    List<WaveTView> getViews();
    void reset(WaveViewConfig config);
    void pause();
    void resume();
    List<List<Short>> getData();
}
