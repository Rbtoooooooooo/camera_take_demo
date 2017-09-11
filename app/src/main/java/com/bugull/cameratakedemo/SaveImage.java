package com.bugull.cameratakedemo;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luopu on 2017/7/31.
 */

public class SaveImage {
    private File crop;
    private File icon;
    private File edit;

    public SaveImage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + "小学期";

//            建立各个文件夹，用于存储图片
            crop = new File(external, rootDir + "/裁剪");
            if (!crop.exists()) {
                crop.mkdirs();
            }

            icon = new File(external, rootDir + "/相片");
            if (!icon.exists()) {
                icon.mkdirs();
            }

            edit = new File(external, rootDir + "/编辑");
            if (!edit.exists()) {
                edit.mkdirs();
            }
        }
    }

    public File cropFile() {
        String file = "";
        if (crop != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            file = (df.format(new Date())).toString() + ".jpg";// new Date()为获取当前系统时间
        }
        return new File(crop, file);
    }

    public File iconFile() {
        String file = "";
        if (icon != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            file = (df.format(new Date())).toString() + ".jpg";// new Date()为获取当前系统时间
        }
        return new File(icon, file);
    }

    public File editFile() {
        String file = "";
        if (edit != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            file = (df.format(new Date())).toString() + ".jpg";// new Date()为获取当前系统时间
        }
        return new File(edit, file);
    }


}