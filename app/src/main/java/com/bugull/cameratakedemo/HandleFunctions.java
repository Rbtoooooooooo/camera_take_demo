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
public class HandleFunctions {

    //    制作海报
    // 制作一张蓝色图片
    public static Bitmap whiteBitmap() {
//        设置该图片的大小
        int pWidth = 1560;
        int pHeight = 1560;
        int[] mPx = new int[pWidth * pHeight];
        Bitmap bitmap = Bitmap.createBitmap(pWidth, pHeight, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < pWidth * pHeight; i++) {
            mPx[i] = Color.BLUE;
        }
//        根据像素点数组得到一张图片
        bitmap.setPixels(mPx, 0, pWidth, 0, 0, pWidth, pHeight);
        return bitmap;
    }

    public static Bitmap makePost(Bitmap sBmp, Bitmap pBmp, int flag, Context context) {

//        得到 被选择的图片的 大小
        int pWidth = pBmp.getWidth();
        int pHeight = pBmp.getHeight();
//        太小不方便操作，则需要重新选
        if (pHeight<750 || pWidth<750) {
            Toast.makeText(context, "尺寸太小", Toast.LENGTH_LONG).show();
            return sBmp;
        }

        int[] oldPexils = new int[pWidth * pHeight];
        int[] newPexils = new int[750*750];
//        得到被选择图片的像素点
        pBmp.getPixels(oldPexils, 0, pWidth, 0, 0, pWidth, pHeight);

//        设置margin
        int v = (pHeight - 750)/2;
        int h = (pWidth - 750)/2;

//        得到该图片中间的750*750个像素点
        for (int i = v; i<v+750; i++ ) {
            for (int j = h; j<h+750; j++) {
                newPexils[(i-v)*750 + j-h] = oldPexils[i*pWidth + j];
            }
        }

//        得到海报的大小及像素点
        int sW = sBmp.getWidth();
        int sH = sBmp.getHeight();
        int[] sourPx = new int[sW * sH];
        sBmp.getPixels(sourPx, 0, sW, 0, 0, sW, sH);

        switch (flag) {
//            将被选择的图片放在左上角
            case 1:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+20)*sW + j+20] = newPexils[i*750 + j];
                    }
                }
                break;
//            将被选择的图片放在右上角
            case 2:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+20)*sW + j+790] = newPexils[i*750 + j];
                    }
                }
                break;
//            将被选择的图片放在左下角
            case 3:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+790)*sW + j+20] = newPexils[i*750 + j];
                    }
                }
                break;
//            将被选择的图片放在右下角
            case 4:
                for (int i = 0; i<750; i++) {
                    for (int j = 0; j<750; j++) {
                        sourPx[(i+790)*sW + j+790] = newPexils[i*750 + j];
                    }
                }
                break;
        }
//        得到新的图片
        sBmp.setPixels(sourPx, 0, sW, 0, 0, sW, sH);

        return sBmp;
    }



    //    用于改变色调，亮度，饱和度
    static ColorMatrix hueM = new ColorMatrix();
    static ColorMatrix satM = new ColorMatrix();
    static ColorMatrix lumM = new ColorMatrix();

//    用于选择滤镜
    public  enum Lvjing
    {
        JINGXIANG,
        HEIBAI,
        DIPIAN,
        HUAIJIU,
        FUDIAO
    };

    public static Bitmap zengqiang(Bitmap bitmap, float nHue, float nSat, float nLum) {
//        得到图片 准备画布 画笔 颜色矩阵
        Bitmap nBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas pCanvas = new Canvas(nBitmap);
        Paint pPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix imageM = new ColorMatrix();

//        得到新的hue值
        hueM.setRotate(0, nHue);
        hueM.setRotate(1, nHue);
        hueM.setRotate(2, nHue);
        imageM.postConcat(hueM);

//        得到新的saturation值
        satM.setSaturation(nSat);
        imageM.postConcat(satM);

//        得到新的lum值
        lumM.setScale(nLum, nLum, nLum, 1);
        imageM.postConcat(lumM);
        pPaint.setColorFilter(new ColorMatrixColorFilter(imageM));

//        用画笔和画布得到新的图片
        pCanvas.drawBitmap(bitmap, 0, 0, pPaint);

        return nBitmap;
    }


    public static Bitmap pikaGoGoGo(Bitmap pBitmap, Lvjing pKind) {

//        得到图片的大小
        int pWidth = pBitmap.getWidth();
        int pHeight = pBitmap.getHeight();
        int nColor, bColor;
        int red, green, blue, a;
        int nr, ng, nb;

//        根据原图绘制一张新图
        Bitmap bitmap = Bitmap.createBitmap(pWidth, pHeight
                , Bitmap.Config.ARGB_8888);

//        得到原图的像素点
        int[] oldPexils = new int[pWidth * pHeight];
        int[] newPexils = new int[pWidth * pHeight];
        pBitmap.getPixels(oldPexils, 0, pWidth, 0, 0, pWidth, pHeight);

//      跟局pkind的值，选择滤镜
        switch (pKind) {
//            浮雕滤镜
            case FUDIAO:
                for (int i = 1; i < pWidth * pHeight; i++) {
//                    获取前一个像素点的值
                    bColor = oldPexils[i - 1];
                    a = Color.alpha(bColor);
                    red = Color.red(bColor);
                    green = Color.green(bColor);
                    blue = Color.blue(bColor);

//                    获取当前像素点的值
                    nColor = oldPexils[i];
                    nr = Color.red(nColor);
                    ng = Color.green(nColor);
                    nb = Color.blue(nColor);

//                  核心算法：该像素点减去前一个像素点对应的rgb值
                    red = (red - nr + 127);
                    green = (green - ng + 127);
                    blue = (blue - nb + 127);
                    if (red > 255) {
                        red = 255;
                    }
                    if (green > 255) {
                        green = 255;
                    }
                    if (blue > 255) {
                        blue = 255;
                    }
//                    得到新的像素点
                    newPexils[i] = Color.argb(a, red, green, blue);
                }
//                根据新的像素点的到一张新的图片
                bitmap.setPixels(newPexils, 0, pWidth, 0, 0, pWidth, pHeight);
                break;

            case HUAIJIU:
//                会就滤镜
                for (int i = 0; i < pWidth * pHeight; i++) {
//                    得到当前像素点的值，以及rgba值
                    nColor = oldPexils[i];
                    a = Color.alpha(nColor);
                    red = Color.red(nColor);
                    green = Color.green(nColor);
                    blue = Color.blue(nColor);

//                    核心算法
                    nr = (int) (0.393 * red + 0.769 * green + 0.189 * blue);
                    ng = (int) (0.349 * red + 0.686 * green + 0.168 * blue);
                    nb = (int) (0.272 * red + 0.534 * green + 0.131 * blue);

                    if (nr > 255) {
                        nr = 255;
                    }
                    if (ng > 255) {
                        ng = 255;
                    }
                    if (nb > 255) {
                        nb = 255;
                    }

//                    得到新的像素点的值
                    newPexils[i] = Color.argb(a, nr, ng, nb);
                }
//                根据新的像素点的值得到新的图片
                bitmap.setPixels(newPexils, 0, pWidth, 0, 0, pWidth, pHeight);
                break;


            case HEIBAI:
//                黑白滤镜

//                存储平均值
                int avg;
                for (int i = 0; i < pWidth * pHeight; i++) {
//                    当前像素点的值及rgba值
                    nColor = oldPexils[i];
                    a = Color.alpha(nColor);
                    red = Color.red(nColor);
                    green = Color.green(nColor);
                    blue = Color.blue(nColor);

//                    得到rgb的平均值
                    avg = (int)(red + green + blue) / 3;
//                    核心算法：根据平均值设置新的像素值
                    if (avg >= 0 && avg < 85) {
                        red = 0;
                        green = 0;
                        blue = 0;
                    } else if (avg >= 85 && avg < 170) {
                        red = 127;
                        green = 127;
                        blue = 127;
                    } else {
                        red = 255;
                        green = 255;
                        blue = 255;
                    }
//                    得到新的像素点
                    newPexils[i] = Color.argb(a, red, green, blue);
                }
//                根据新的像素点得到新的图片
                bitmap.setPixels(newPexils, 0, pWidth, 0, 0, pWidth, pHeight);
                break;


            case DIPIAN:
//                底片
                for (int i = 0; i < pWidth * pHeight; i++) {
//                    当前像素点及rgba值
                    nColor = oldPexils[i];
                    red = Color.red(nColor);
                    green = Color.green(nColor);
                    blue = Color.blue(nColor);
                    a = Color.alpha(nColor);

//                    核心算法
                    red = 255 - red;
                    green = 255 - green;
                    blue = 255 - blue;

                    if (red > 255) {
                        red = 255;
                    } else if (red < 0) {
                        red = 0;
                    }
                    if (green > 255) {
                        green = 255;
                    } else if (green < 0) {
                        green = 0;
                    }
                    if (blue > 255) {
                        blue = 255;
                    } else if (blue < 0) {
                        blue = 0;
                    }
//                    得到新的像素点
                    newPexils[i] = Color.argb(a, red, green, blue);
                }
//                根据新的像素点获取新的图片
                bitmap.setPixels(newPexils, 0, pWidth, 0, 0, pWidth, pHeight);
                break;

            case JINGXIANG:
//                镜像
//                核心算法
                for (int i = 0; i < pHeight; i++) {
                    for (int j = 0; j < pWidth; j++) {
                        newPexils[i*pWidth+j] = oldPexils[i*pWidth + pWidth - j - 1];
                    }
                }

//                根据新的像素点获取新的图片
                bitmap.setPixels(newPexils, 0, pWidth, 0, 0, pWidth, pHeight);
                break;
        }

//        将新的图片返回
        return bitmap;

    }

    public static void saveImage(Bitmap bitmap, Context context){

//        new一个文件，用于保存
        File file=new SaveImage().editFile();//将要保存图片的路径和图片名称
        try {
//            将图片保存下来
            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            通过Toast显示保存路径
            Toast.makeText(context, file.toString(), Toast.LENGTH_LONG).show();
            bos.flush();
            bos.close();

        } catch (IOException e) {
//            捕捉文件操作错误
            e.printStackTrace();
        }
    }


    public static Bitmap addText1(Context pContext, Bitmap pBitmap, String pText,
                                  int pSize, int pColor, int pL, int pT) {
//        new一个画笔，设置颜色，字体大小，偏移量
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(pColor);
        paint.setTextSize(dp(pContext, pSize));
        Rect pRect = new Rect();
        paint.getTextBounds(pText, 0, pText.length(), pRect);
        return addText2(pContext, pBitmap, pText, paint, pRect,
                dp(pContext, pL),
                dp(pContext, pT) + pRect.height());
    }

    //图片上绘制文字
    private static Bitmap addText2(Context context, Bitmap bitmap, String text,
                                   Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
//        根据原先的设置，绘制新的图片
        canvas.drawText(text, paddingLeft, paddingTop, paint);
//        返回图片
        return bitmap;
    }

    public static int dp(Context context, float dp) {
//        手机屏幕大小
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

//    保存图片




}