package com.bugull.cameratakedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2014/12/20 0020.
 */
public class ImageHelper {

    static ColorMatrix hueMatrix = new ColorMatrix();
    static ColorMatrix saturationMatrix = new ColorMatrix();
    static ColorMatrix lumMatrix = new ColorMatrix();

    public static enum FiterKind
    {
        HUAIJIU,
        FUDIAO,
        JINGXIANG,
        HEIBAI,
        DIPIAN
    };

//    btn_huaijiu, btn_fudiao, btn_guangzhao, btn_heibai, btn_dipian;

    public static Bitmap handleImageEffect(Bitmap bm, float hue, float saturation, float lum, int flag) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix imageMatrix = new ColorMatrix();

        hueMatrix.setRotate(0, hue);
        hueMatrix.setRotate(1, hue);
        hueMatrix.setRotate(2, hue);
        imageMatrix.postConcat(hueMatrix);

        saturationMatrix.setSaturation(saturation);
        imageMatrix.postConcat(saturationMatrix);

        lumMatrix.setScale(lum, lum, lum, 1);
        imageMatrix.postConcat(lumMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));

        canvas.drawBitmap(bm, 0, 0, paint);

        return bmp;
    }


    public static Bitmap pikaGoGoGo(Bitmap bm, FiterKind mKind) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        int color, colorBefore;
        int r, g, b, a;
        int r1, g1, b1;


        Bitmap bmp = Bitmap.createBitmap(width, height
                , Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);


        switch (mKind) {
            case HUAIJIU:
                for (int i = 0; i < width * height; i++) {
                    color = oldPx[i];
                    a = Color.alpha(color);
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);

                    r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                    g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                    b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                    if (r1 > 255) {
                        r1 = 255;
                    }
                    if (g1 > 255) {
                        g1 = 255;
                    }
                    if (b1 > 255) {
                        b1 = 255;
                    }

                    newPx[i] = Color.argb(a, r1, g1, b1);
                }
                bmp.setPixels(newPx, 0, width, 0, 0, width, height);
                break;
            case FUDIAO:
                for (int i = 1; i < width * height; i++) {
                    colorBefore = oldPx[i - 1];
                    a = Color.alpha(colorBefore);
                    r = Color.red(colorBefore);
                    g = Color.green(colorBefore);
                    b = Color.blue(colorBefore);

                    color = oldPx[i];
                    r1 = Color.red(color);
                    g1 = Color.green(color);
                    b1 = Color.blue(color);

                    r = (r - r1 + 127);
                    g = (g - g1 + 127);
                    b = (b - b1 + 127);
                    if (r > 255) {
                        r = 255;
                    }
                    if (g > 255) {
                        g = 255;
                    }
                    if (b > 255) {
                        b = 255;
                    }
                    newPx[i] = Color.argb(a, r, g, b);
                }
                bmp.setPixels(newPx, 0, width, 0, 0, width, height);
                break;
            case JINGXIANG:
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        newPx[i*width+j] = oldPx[i*width + width - j - 1];
                    }
                }
                bmp.setPixels(newPx, 0, width, 0, 0, width, height);
                break;
            case HEIBAI:
                int avg;
                for (int i = 0; i < width * height; i++) {
                    color = oldPx[i];
                    a = Color.alpha(color);
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);

                    avg = (int)(r + g + b) / 3;
                    if (avg >= 0 && avg < 85) {
                        r = 0;
                        g = 0;
                        b = 0;
                    } else if (avg >= 85 && avg < 170) {
                        r = 127;
                        g = 127;
                        b = 127;
                    } else {
                        r = 255;
                        g = 255;
                        b = 255;
                    }

                    newPx[i] = Color.argb(a, r, g, b);
                }
                bmp.setPixels(newPx, 0, width, 0, 0, width, height);
                break;
            case DIPIAN:
                for (int i = 0; i < width * height; i++) {
                    color = oldPx[i];
                    r = Color.red(color);
                    g = Color.green(color);
                    b = Color.blue(color);
                    a = Color.alpha(color);

                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    if (r > 255) {
                        r = 255;
                    } else if (r < 0) {
                        r = 0;
                    }
                    if (g > 255) {
                        g = 255;
                    } else if (g < 0) {
                        g = 0;
                    }
                    if (b > 255) {
                        b = 255;
                    } else if (b < 0) {
                        b = 0;
                    }
                    newPx[i] = Color.argb(a, r, g, b);
                }
                bmp.setPixels(newPx, 0, width, 0, 0, width, height);
                break;

        }

        return bmp;

    }



    /*
     神奇的代码，不知道错哪了
     日后有空再查，可对照这个类中的pikaGoGoGo函数中case HUIJIU部分进行查找错误
     */
//    public static Bitmap pikaGoGoGo(Bitmap bm) {
//
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//
//        int color;
//        int r, g, b, a;
//        int[] oldPx = new int[width * height];
//        int[] newPx = new int[width * height];
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);
//
//        for (int i = 0; i < width * height; i++) {
//            color = oldPx[i];
//            r = Color.red(color);
//            g = Color.green(color);
//            b = Color.blue(color);
//            a = Color.alpha(color);
//
//            r = 255 - r;
//            g = 255 - g;
//            b = 255 - b;
//
//            if (r > 255) {
//                r = 255;
//            } else if (r < 0) {
//                r = 0;
//            }
//            if (g > 255) {
//                g = 255;
//            } else if (g < 0) {
//                g = 0;
//            }
//            if (b > 255) {
//                b = 255;
//            } else if (b < 0) {
//                b = 0;
//            }
//
//            newPx[i] = Color.argb(a, r, g, b );
//        }
//
//        bitmap.setPixels(newPx, 0, width, 0, 0, width, height);
//        return bitmap;
//    }


    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
                                           int size, int color, int paddingLeft, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                dp2px(context, paddingTop) + bounds.height());
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public static void saveBitmapFile(Bitmap bitmap, Context context){

        File file=new FileStorage().createEditFile();//将要保存图片的路径和图片名称
        try {
            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Toast.makeText(context, "已保存至"+file.toString(), Toast.LENGTH_LONG).show();
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //    制作海报
    public static Bitmap createBlankBitmap() {

        int width = 1560;
        int height = 1560;
        int[] px = new int[width * height];

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < width * height; i++) {
            px[i] = Color.WHITE;
        }

        bmp.setPixels(px, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap createPost(Bitmap sourBitmap, Bitmap partBitmap, int flag, Context context) {


        int pWidth = partBitmap.getWidth();
        int pHeight = partBitmap.getHeight();
        if (pHeight<750 || pWidth<750) {
            Toast.makeText(context, "图片尺寸不符，请重新选择", Toast.LENGTH_LONG).show();
            return sourBitmap;
        }

        Bitmap newBitmap = Bitmap.createBitmap(sourBitmap.getWidth(), sourBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[pWidth * pHeight];
        int[] newPx = new int[750*750];

        partBitmap.getPixels(oldPx, 0, pWidth, 0, 0, pWidth, pHeight);

        int v_margin = (pHeight - 750)/2;
        int h_margin = (pWidth - 750)/2;

        for (int i = v_margin; i<v_margin+750; i++ ) {
            for (int j = h_margin; j<h_margin+750; j++) {
                newPx[(i-v_margin)*750 + j-h_margin] = oldPx[i*pWidth + j];
            }
        }

        int sWidth = sourBitmap.getWidth();
        int sHeight = sourBitmap.getHeight();
        int[] sourPx = new int[sWidth * sHeight];
        sourBitmap.getPixels(sourPx, 0, sWidth, 0, 0, sWidth, sHeight);

        switch (flag) {
            case 1:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+20)*sWidth + j+20] = newPx[i*750 + j];
                    }
                }
                break;
            case 2:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+20)*sWidth + j+790] = newPx[i*750 + j];
                    }
                }
                break;
            case 3:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+790)*sWidth + j+20] = newPx[i*750 + j];
                    }
                }
                break;
            case 4:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+790)*sWidth + j+790] = newPx[i*750 + j];
                    }
                }
                break;
        }

        sourBitmap.setPixels(sourPx, 0, sWidth, 0, 0, sWidth, sHeight);

        return sourBitmap;
    }


}