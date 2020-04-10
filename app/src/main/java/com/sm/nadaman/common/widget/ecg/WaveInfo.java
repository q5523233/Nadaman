package com.sm.nadaman.common.widget.ecg;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

public class WaveInfo {
    private List<Short> datas = new ArrayList<>();//所有的数据
    String title;
    private int total;
    private int maxReMainSize = 2500;//最近10秒数据
    private Path path;

    public List<Short> getDatas() {
        return datas;
    }

    public WaveInfo(String title) {
        this.title = title;
        path = new Path();
    }

    public Path getPath() {
        return path;
    }

    public void addNum(short a) {
        if (datas.size() >= maxReMainSize) {
            int index = total % maxReMainSize;
            datas.set(index, a);
        } else
            datas.add(a);
        total++;
    }

    /**
     * @param index 小于等于total-1 从0开始
     * @return
     */
    private int getMapperIndex(int index) {
        return index % maxReMainSize;
    }

    int getMapperData(int index) {
        return datas.get(getMapperIndex(index));
//return 0;
    }

    void setMaxReMainSize(int maxReMainSize) {
        this.maxReMainSize = maxReMainSize;
    }

    public void clear() {
        datas.clear();
        total = 0;
    }
}
