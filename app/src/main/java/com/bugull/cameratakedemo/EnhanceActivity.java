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

    private ImageView mImageView;
    private SeekBar mSeekbarhue,mSeekbarSaturation, mSeekbarLum;
    private Button btn;
    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private float mHue,mStauration, mLum;
    private Bitmap bitmap, newBitmap;
    private int flag = -1;

    private String path;
    private Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enhance);

        mIntent = getIntent();
        path = mIntent.getStringExtra("path");
        bitmap = BitmapFactory.decodeFile(path);

        mImageView = (ImageView) findViewById(R.id.enhanceImageView);

        mSeekbarhue = (SeekBar) findViewById(R.id.hueSeekBar);
        mSeekbarSaturation = (SeekBar) findViewById(R.id.saturationSeekBar);
        mSeekbarLum = (SeekBar) findViewById(R.id.luminositySeekBar);
        btn = (Button) findViewById(R.id.saveButton);

        mSeekbarhue.setOnSeekBarChangeListener(this);
        mSeekbarSaturation.setOnSeekBarChangeListener(this);
        mSeekbarLum.setOnSeekBarChangeListener(this);
        btn.setOnClickListener(this);

        mSeekbarhue.setMax(MAX_VALUE);
        mSeekbarSaturation.setMax(MAX_VALUE);
        mSeekbarLum.setMax(MAX_VALUE);

        mSeekbarhue.setProgress(MID_VALUE);
        mSeekbarSaturation.setProgress(MID_VALUE);
        mSeekbarLum.setProgress(MID_VALUE);

        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.hueSeekBar:
                flag = 0;
                mHue = (progress - MID_VALUE) * 1.0F / MID_VALUE * 180;
                break;
            case R.id.saturationSeekBar:
                flag = 1;
                mStauration = progress * 1.0F / MID_VALUE;
                break;
            case R.id.luminositySeekBar:
                flag = 2;
                mLum = progress * 1.0F / MID_VALUE;
                break;
        }
        newBitmap = ImageHelper.handleImageEffect(bitmap, mHue, mStauration, mLum, flag);
        mImageView.setImageBitmap(newBitmap);

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
            case R.id.saveButton:
                ImageHelper.saveBitmapFile(newBitmap, this);
                break;
        }
    }
}
