package com.bugull.cameratakedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

public class EnhanceActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{

    private ImageView piv;
    private SeekBar hue, sat, lum;
    private Button pButton;
    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private float mHue,mStauration, mLum;
    private Bitmap pBitmap, nBitmap;
    private String path;
    private Intent pIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zengqiang);

        init();
    }

    private void init() {
//        获取path，并将path下的图片转换为Bitmap类型数据
        pIntent = getIntent();
        path = pIntent.getStringExtra("path");
        pBitmap = BitmapFactory.decodeFile(path);

//        绑定
        piv = (ImageView) findViewById(R.id.enhanceImageView);
        hue = (SeekBar) findViewById(R.id.hueSeekBar);
        sat = (SeekBar) findViewById(R.id.saturationSeekBar);
        lum = (SeekBar) findViewById(R.id.luminositySeekBar);
        pButton = (Button) findViewById(R.id.sbtn);

//        设置监听函数
        hue.setOnSeekBarChangeListener(this);
        sat.setOnSeekBarChangeListener(this);
        lum.setOnSeekBarChangeListener(this);
        pButton.setOnClickListener(this);

//        设置最大值
        hue.setMax(MAX_VALUE);
        sat.setMax(MAX_VALUE);
        lum.setMax(MAX_VALUE);

//        设置初始值
        hue.setProgress(MID_VALUE);
        sat.setProgress(MID_VALUE);
        lum.setProgress(MID_VALUE);

        piv.setImageBitmap(pBitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.hueSeekBar:
//               得到新的hue值
                mHue = (progress - MID_VALUE) * 1.0F / MID_VALUE * 180;
                break;
            case R.id.saturationSeekBar:
//               得到新的saturation值
                mStauration = progress * 1.0F / MID_VALUE;
                break;
            case R.id.luminositySeekBar:
//               得到新的saturation值
                mLum = progress * 1.0F / MID_VALUE;
                break;
        }
        nBitmap = HandleFunctions.zengqiang(pBitmap, mHue, mStauration, mLum);
        piv.setImageBitmap(nBitmap);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            保存图片
            case R.id.sbtn:
                HandleFunctions.saveImage(nBitmap, this);
                break;
        }
    }
}
