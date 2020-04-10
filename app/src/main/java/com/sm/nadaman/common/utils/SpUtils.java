package com.sm.nadaman.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.techne.nomnompos.app.App;

import java.util.List;

public class SpUtils {
    SharedPreferences sp;
    String name = "kangBoEr";
    private static SpUtils instance;

    public static SpUtils getInstance() {
        if (instance == null) {
            synchronized (SpUtils.class) {
                if (instance == null) {
                    instance = new SpUtils();
                }
            }
        }
        return instance;
    }

    private SpUtils() {
        sp = App.Companion.getApp().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public void putObject(String key, Object value) {
        String json = new Gson().toJson(value);
        putString(key, json);
    }

    public void putList(String key, List value) {
        String json = new Gson().toJson(value);
        putString(key, json);
    }

    public <T> T readObject(String key, Class<T> clazz) {
        String s = sp.getString(key, null);
        if (s == null) {
            return null;
        }
        try {
            return new Gson().fromJson(s, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public String readString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }
}
