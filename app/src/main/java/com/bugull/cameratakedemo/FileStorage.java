package com.bugull.cameratakedemo;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luopu on 2017/7/31.
 */

public class FileStorage {
    private File cropIconDir;
    private File iconDir;
    private File editDir;

    public FileStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + "demo";

            cropIconDir = new File(external, rootDir + "/crop");
            if (!cropIconDir.exists()) {
                cropIconDir.mkdirs();
            }

            iconDir = new File(external, rootDir + "/icon");
            if (!iconDir.exists()) {
                iconDir.mkdirs();
            }

            editDir = new File(external, rootDir + "/edit");
            if (!editDir.exists()) {
                editDir.mkdirs();
            }
        }
    }

    public File createCropFile() {
        String fileName = "";
        if (cropIconDir != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            fileName = (df.format(new Date())).toString() + ".jpg";// new Date()为获取当前系统时间
        }
        return new File(cropIconDir, fileName);
    }

    public File createIconFile() {
        String fileName = "";
        if (iconDir != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            fileName = (df.format(new Date())).toString() + ".jpg";// new Date()为获取当前系统时间
        }
        return new File(iconDir, fileName);
    }

    public File createEditFile() {
        String fileName = "";
        if (editDir != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            fileName = (df.format(new Date())).toString() + ".jpg";// new Date()为获取当前系统时间
        }
        return new File(editDir, fileName);
    }


}