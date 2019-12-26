package com.sm.nadaman.common.ecg;

import android.os.Environment;

import java.io.File;

/**
 * Created by sm on 2018/4/25.
 */

public class Config {
    public static final String KEY_SETTING_CONFIG = "setting_config";
    public static String KEY_LAYOUT_FLAG = "layout_flag";
    public static String KEY_LAYOUT_GAIN = "layout_gain";
    public static String KEY_LAYOUT_SPEED = "layout_speed";
    /**
     * 布局方式
     */
    public static final int LAYOUT＿TYPE_12x1 = 1;
    public static final int LAYOUT＿TYPE_6x2 = 2;
    public static final int LAYOUT＿TYPE_6x2_1 = 3;
    public static final int LAYOUT＿TYPE_3x4 = 4;
    public static final int LAYOUT＿TYPE_3x4_1 = 5;
    public static final int LAYOUT＿TYPE_3x4_3 = 6;
    /**
     * PDF布局
     */
    public static final String[] PDF_LAYOUT = {"12ch * 1", "1ch", "3ch * 4 + (1ch)", "3ch * 4 + (3ch)", "6ch * 2 + (1ch)"};
    /**
     * 纸度比例
     */
    public static final int LAYOUT_SPEED_WAVE_SPEED_12_5 = 1;
    public static final int LAYOUT_SPEED_WAVE_SPEED_25 = 2;
    public static final int LAYOUT_SPEED_WAVE_SPEED_50 = 3;
    /**
     * 增益比例
     */
    public static final int LAYOUT_GAIN_5 = 1;
    public static final int LAYOUT_GAIN_10 = 2;
    public static final int LAYOUT_GAIN_20 = 3;


    public static final String pdf_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ShenMai";
    public static final String pdf_name = "/mypdf.pdf";
    public static final String jpg_name = "/my.jpg";


    public static String getPDFPath() {
        File file = new File(pdf_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return pdf_path + pdf_name;
    }

    public static String getJPGPath() {
        File file = new File(pdf_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return pdf_path + jpg_name;
    }


    /*应用的sd卡路径*/
    public static String PATH_SD_APPPAATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ShenMai/";

    static {
        File file = new File(PATH_SD_APPPAATH);
        if (!file.exists()) {
            file.mkdirs();//创建目录
        }
    }
}
