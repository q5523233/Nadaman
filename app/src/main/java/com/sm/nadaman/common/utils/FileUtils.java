package com.sm.nadaman.common.utils;

import android.content.Context;

import java.io.File;

/**
 * <br/>
 */
public class FileUtils {

    /**
     * 下载目录
     */
    public static String downloadDir = "/download/";

    /**
     * APP 图片文件夹
     */
    public static String images = "/images/";

    /**
     * 创建存储目录
     * @param context
     * @param dir               目录  （ps:请参照静态变量）
     * @return                  目录
     */
    public static String createDir(Context context, String dir) {
        String path;
        if(context.getExternalFilesDir(null) == null) {
            path = context.getFilesDir() + dir;
        } else {
            path = context.getExternalFilesDir(null) + dir;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    /**
     * 创建下载App目录
     * @param context
     * @param fileName                  文件名
     * @return
     */
    public static String createDownloadAppDir(Context context, String fileName) {
        String path = createDir(context,FileUtils.downloadDir);
        String mFileName = path + fileName;
        // 判断文件是否存在，如果存在则删除
        if(checkFile(mFileName)) {
            new File(mFileName).delete();
        }
        return path;
    }

    /**
     * 判断某个文件是否存在
     * @param path
     */
    public static boolean checkFile(String path) {
        File file = new File(path);
        return  file.exists();
    }
}
