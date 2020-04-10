package com.sm.nadaman.common.widget.ecg;

import android.graphics.Color;


public class WaveViewConfig {
    private float gain;
    static int secCount = 125;//每秒钟上位机接收到的数据
    private int pagesize = 1;//分成几页显示
    static final int valuePerMV = 1310;//1mv的最大值与最小值差 每个设备不一样

    public WaveViewConfig setPagesize(int pagesize) {
        this.pagesize = pagesize;
        return this;
    }

    public int getPagesize() {
        return pagesize;
    }

    public float getGain() {
        return gain;
    }


    float getSpeed() {
        return speed;
    }

    int getSecCount() {
        return secCount;
    }

    //    public static final float SPEED_SLOW = 12.5f;
//    public static final float SPEED_NORMAL = 25f;
//    public static final float SPEED_FAST = 50f;
    private float speed;

    public WaveViewConfig setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

//    public static final float Gain_2_5 = 0.5f;
//    public static final float Gain_5 = 1;
//    public static final float Gain_10 = 2;
//    public static final float Gain_20 = 4;

    public WaveViewConfig setGain(float gain) {
        this.gain = gain;
        return this;
    }

//    public WaveViewConfig setStyle(int style) {
//        return this;
//    }

//    public static final int STYLE_BLACK = 0;
//    public static final int STYLE_WHITE = 1;
    /**
     * colors1 = Color.rgb(28, 28, 28);
     * colors2 = Color.rgb(48, 48, 48);
     * color = Color.BLACK;
     */
    static final int[] defaultColors = new int[]{
            Color.WHITE,
//            Color.rgb(28, 28, 28),
//            Color.rgb(48, 48, 48),
            Color.argb(16, 0, 0, 0),
            Color.argb(16 * 8, 255, 0, 0),
            Color.BLACK,
            Color.BLACK
    };


}
