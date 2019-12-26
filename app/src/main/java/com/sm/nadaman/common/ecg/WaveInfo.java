package com.sm.nadaman.common.ecg;



import com.sm.nadaman.common.widget.ecg.BackLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sm on 2018/4/20.
 * 获取布局的X Y位置
 */

public class WaveInfo {
    private WaveInfo() {
        for (int i = 0; i < 15; i++) {
            mLineInfos.add(new LineInfo());
        }
    }

    private static WaveInfo mWanveInfo;

    public static synchronized WaveInfo getInstance() {
        if (mWanveInfo == null) {
            mWanveInfo = new WaveInfo();
        }
        return mWanveInfo;
    }

    private List<LineInfo> mLineInfos = new ArrayList<>();

    private BackLayout backLayout;
    private WaveLayout mWaveLayout = WaveLayout.LAYOUT_12x1;//布局方式
    private WaveGain mWaveGain = WaveGain.WAVE_GAIN_10;//默认 增益量

    public WaveSpeed getWaveSpeed() {
        return mWaveSpeed;
    }

    private WaveSpeed mWaveSpeed = WaveSpeed.WAVE_SPEED_25;//默认 纸速值

    public BackLayout getBackLayout() {
        return backLayout;
    }

    public WaveGain getWaveGain() {
        return mWaveGain;
    }

    public WaveLayout getWaveLayout() {
        return mWaveLayout;
    }



    /**
     * 获取第几条线的数据
     *
     * @param index
     * @return
     */
    public LineInfo getLineInfo(int index) {
        if (index < 0 || index >= mLineInfos.size())
            return new LineInfo();
        return mLineInfos.get(index);
    }

    /**
     * 第一步
     *
     * @param backLayout
     */
    public void init(BackLayout backLayout) {
        this.backLayout = backLayout;
    }

    /**
     * 设置增益
     *
     * @param waveGain
     */
    public void setWaveGain(WaveGain waveGain) {
        mWaveGain = waveGain;
    }

    public void setWaveGainType(int gainType) {
        if (gainType == Config.LAYOUT_GAIN_5) {
            mWaveGain = WaveGain.WAVE_GAIN_5;
        } else if (gainType == Config.LAYOUT_GAIN_10) {
            mWaveGain = WaveGain.WAVE_GAIN_10;
        } else if (gainType == Config.LAYOUT_GAIN_20) {
            mWaveGain = WaveGain.WAVE_GAIN_20;
        }

    }

    /**
     * 设置纸速
     *
     * @param waveSpeed
     */
    public void setWaveSpeed(WaveSpeed waveSpeed) {
        mWaveSpeed = waveSpeed;
    }

    public void setWaveSpeedType(int typeSpeed) {

        if (typeSpeed == Config.LAYOUT_SPEED_WAVE_SPEED_12_5) {
            mWaveSpeed = WaveSpeed.WAVE_SPEED_12_5;
        } else if (typeSpeed == Config.LAYOUT_SPEED_WAVE_SPEED_25) {
            mWaveSpeed = WaveSpeed.WAVE_SPEED_25;
        } else if (typeSpeed == Config.LAYOUT_SPEED_WAVE_SPEED_50) {
            mWaveSpeed = WaveSpeed.WAVE_SPEED_50;
        }
    }


    public void initLayout() {
        setLayout(mWaveLayout);
    }


    /**
     * 初始化显示样式 ，先设置了纸速和增益再调用这方法
     *
     * @param layout
     */
    public void setLayout(WaveLayout layout) {
        this.mWaveLayout = layout;
        int gain = 3;

        switch (layout) {
            case LAYOUT_12x1:
                for (int i = 0; i < 12; i++) {
                    mLineInfos.get(i).startX = backLayout.getRectLeft() + 0;
                    mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * 2 * i - 5 * gain);
                    mLineInfos.get(i).lineWidth = backLayout.getWorkWidth();
                    mLineInfos.get(i).lineHeight = (int) getGridWidth(5 * 8);
                }
                break;
            case LAYOUT_6x2:
                for (int i = 0; i < 12; i++) {
                    if (i % 2 == 0) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + 0;
                    } else {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 2;
                    }
                    mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (2 * 2 * (i / 2) + 1 - gain));
                    mLineInfos.get(i).lineWidth = backLayout.getWorkWidth() / 2;
                    mLineInfos.get(i).lineHeight = (int) getGridWidth(5 * 8);
                }


                break;
            case LAYOUT_3x4:

                for (int i = 0; i < 12; i++) {
                    if (i == 0 || i == 4 || i == 8) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft();
                    } else if (i == 1 || i == 5 || i == 9) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4;
                    } else if (i == 2 || i == 6 || i == 10) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4 * 2;
                    } else {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4 * 3;
                    }

                    if (i / 4 == 2) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * 3 * 5 + 5 * 2 * 2 - 5 * gain);
                    } else if (i / 4 == 1) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * 3 * 3 + 5 * 2 - 5 * gain);
                    } else {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * 3 - 5 * gain);
                    }
                    mLineInfos.get(i).lineWidth = backLayout.getWorkWidth() / 4;
                    mLineInfos.get(i).lineHeight = (int) getGridWidth(5 * 8);
                }

                break;
            case LAYOUT_6x2_1:
                for (int i = 0; i < 13; i++) {
                    if (i % 2 == 0) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + 0;
                    } else {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 2;
                    }
                    float f = 1.2f;
                    if (i == 0 || i == 1) {

                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (1 * f + 0 - gain));
                    } else if (i == 2 || i == 3) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (2 * f + 1 * 2 - gain));
                    } else if (i == 4 || i == 5) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (3 * f + 2 * 2 - gain));
                    } else if (i == 6 || i == 7) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (4 * f + 3 * 2 - gain));
                    } else if (i == 8 || i == 9) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (5 * f + 4 * 2 - gain));
                    } else if (i == 10 || i == 11) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (6 * f + 5 * 2 - gain));
                    } else {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (7 * f + 6 * 2 - gain));
                    }


                    if (i < 12) {
                        mLineInfos.get(i).lineWidth = backLayout.getWorkWidth() / 2;

                    } else {
                        mLineInfos.get(i).lineWidth = backLayout.getWorkWidth();
                    }
                    mLineInfos.get(i).lineHeight = (int) getGridWidth(5 * 8);
                }


                break;
            case LAYOUT_3x4_1:
                for (int i = 0; i < 13; i++) {
                    if (i == 0 || i == 4 || i == 8 || i == 12) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft();
                    } else if (i == 1 || i == 5 || i == 9) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4;
                    } else if (i == 2 || i == 6 || i == 10) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4 * 2;
                    } else {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4 * 3;
                    }

                    if (i < 4) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (2 * 1 - gain));
                    } else if (i < 8) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (2 * 4 - gain));
                    } else if (i < 12) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (2 * 7 - gain));
                    } else {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (2 * 10 - gain));
                    }

                    if (i < 12) {
                        mLineInfos.get(i).lineWidth = backLayout.getWorkWidth() / 4;
                    } else {
                        mLineInfos.get(i).lineWidth = backLayout.getWorkWidth();
                    }
                    mLineInfos.get(i).lineHeight = (int) getGridWidth(5 * 8);
                }
                break;
            case LAYOUT_3x4_3:
                for (int i = 0; i < 15; i++) {
                    if (i == 0 || i == 4 || i == 8 || i >= 12) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft();
                    } else if (i == 1 || i == 5 || i == 9) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4;
                    } else if (i == 2 || i == 6 || i == 10) {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4 * 2;
                    } else {
                        mLineInfos.get(i).startX = backLayout.getRectLeft() + backLayout.getWorkWidth() / 4 * 3;
                    }

                    if (i < 4) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (1 - gain));
                    } else if (i < 8) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (5 - gain));
                    } else if (i < 12) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (9 - gain));
                    } else if (i < 13) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (13 - gain));
                    } else if (i < 14) {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (17 - gain));
                    } else {
                        mLineInfos.get(i).startY = backLayout.getRectTop() + (int) getGridWidth(5 * (21 - gain));
                    }

                    if (i < 12) {
                        mLineInfos.get(i).lineWidth = backLayout.getWorkWidth() / 4;
                    } else {
                        mLineInfos.get(i).lineWidth = backLayout.getWorkWidth();
                    }
                    mLineInfos.get(i).lineHeight = (int) getGridWidth(5 * 8);
                }
                break;
        }
    }

    /**
     * 文字大小
     *
     * @return
     */
    public float getTextSize() {
        return backLayout.getGridWidth() * 3;
    }

    /**
     * 一定距离有多少个格子
     *
     * @param width
     * @return
     */
    public int getGridNum(int width) {
        if (width == 0)
            return 0;
        return (int) (width / backLayout.getGridWidth());
    }

    public int getRectRight(){
        return backLayout.getRectRight();
    }

    public int getRectLeft(){
        return backLayout.getRectLeft();
    }

    /**
     * 速度 每秒/格mm
     *
     * @return
     */
    public float getSpeed() {
        if (mWaveSpeed == WaveSpeed.WAVE_SPEED_12_5) {
            return 12.5f;
        } else if (mWaveSpeed == WaveSpeed.WAVE_SPEED_25) {
            return 25f;
        }
        return 50f;
    }


    /**
     * 获取一定格子数的宽度
     *
     * @param toNum
     * @return
     */
    public float getGridWidth(int toNum) {
        return backLayout.getGridWidth() * toNum;
    }

    /**
     * 获取一定格子数的宽度
     *
     * @param toNum
     * @return
     */
    public float getGridWidth(float toNum) {
        return backLayout.getGridWidth() * toNum;
    }

    public float getTextStartX(float textWidth) {
        return (getGridHeight(10) - textWidth) / 2;
    }

    public float getTextStartY(int textLenght, int index) {
        return mLineInfos.get(index).lineHeight / 2;
    }

    /**
     * 获取一定格子数的高度
     *
     * @param toNum
     * @return
     */
    public float getGridHeight(int toNum) {
        return getGridWidth(toNum);
    }

    public enum WaveLayout {
        LAYOUT_12x1,
        LAYOUT_6x2,
        LAYOUT_3x4,
        LAYOUT_6x2_1,
        LAYOUT_3x4_1,
        LAYOUT_3x4_3,
    }

    public enum WaveGain {
        WAVE_GAIN_5,
        WAVE_GAIN_10,
        WAVE_GAIN_20,
    }

    public enum WaveSpeed {
        WAVE_SPEED_12_5,
        WAVE_SPEED_25,
        WAVE_SPEED_50,
    }

    public class LineInfo {
        public int startX;
        public int startY;
        public int lineWidth;
        public int lineHeight;

        @Override
        public String toString() {
            return "LineInfo{" +
                    "startX=" + startX +
                    ", startY=" + startY +
                    ", lineWidth=" + lineWidth +
                    ", lineHeight=" + lineHeight +
                    '}';
        }
    }

    public void reset(){
        mWanveInfo=null;
    }
}
