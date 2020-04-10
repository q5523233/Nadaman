package com.sm.nadaman.common.helper;

import android.content.Context;


import com.sm.nadaman.R;
import com.sm.nadaman.common.widget.ecg.WaveInfo;
import com.sm.nadaman.common.widget.ecg.WaveTView;
import com.sm.nadaman.common.widget.ecg.WaveViewConfig;
import com.techne.nomnompos.app.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EcgViewHelper implements Ecg12ViewDataHelper {
    private List<WaveTView> views;
    private String[] titles;
    private Context context;
    private short[][] dest;

    {
        titles = App.Companion.getApp().getResources().getStringArray(R.array.ecgTitles);
    }

    EcgViewHelper(Context context, final WaveViewConfig config) {
        this.context = context;
        views = new ArrayList<>();
        createViews(config);
    }


    EcgViewHelper(final WaveTView[] waveTView, final WaveViewConfig config) {
        views = new ArrayList<>();
        views.addAll(Arrays.asList(waveTView));
        createViews(config);
    }

    private void createViews(final WaveViewConfig config) {
        int pagesize = config.getPagesize();
        dest = new short[pagesize][];
        int i1 = titles.length / pagesize;
        boolean needCreate = views.size() == 0;
        for (int i = 0; i < pagesize; i++) {
            dest[i] = new short[i1];
            final String[] dest = new String[i1];
            System.arraycopy(titles, i * i1, dest, 0, i1);
            final WaveTView view;
            if (needCreate) {
                view = new WaveTView(context);
//                if (pagesize == 1) {
//                    view.setDragEnable(true);
//                } else {
//                    view.setDragEnable(false);
//                }
                views.add(view);
            } else {
                view = views.get(i);
            }
            view.setListener(new WaveTView.PrepareListener() {
                @Override
                public void onReady() {
                    view.build(config, dest);
                }
            });
        }
    }


    @Override
    public synchronized void addData(short[] data) {
        for (int i = 0; i < views.size(); i++) {
            System.arraycopy(data, i * data.length / views.size(), dest[i], 0, data.length / views.size());
            views.get(i).add(dest[i]);
        }
    }

    @Override
    public List<WaveTView> getViews() {
        return views;
    }

    @Override
    public synchronized void reset(WaveViewConfig config) {
        if (config.getPagesize() != views.size()) {
            //config change
            for (int i = 0; i < views.size(); i++) {
                views.get(i).pause();
            }
            views.clear();
            createViews(config);
            return;
        }
        for (int i = 0; i < views.size(); i++) {
            views.get(i).reBuild(config);
//            views.get(i).reConfig(config,titles);
        }
    }

    @Override
    public void pause() {
        for (WaveTView view : views) {
            view.pause();
        }
    }

    @Override
    public void resume() {
        for (WaveTView view : views) {
            view.resume();
        }
    }

    @Override
    public List<List<Short>> getData() {
        ArrayList<List<Short>> data = new ArrayList<>();
        for (WaveTView view : views) {
            List<WaveInfo> waveInfos = view.getWaveInfos();
            for (WaveInfo waveInfo : waveInfos) {
                data.add(waveInfo.getDatas());
            }
        }
        return data;
    }


}
