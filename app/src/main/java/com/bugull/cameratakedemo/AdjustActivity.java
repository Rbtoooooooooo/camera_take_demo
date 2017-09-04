package com.bugull.cameratakedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.app.Activity;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class AdjustActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private int minWidth = 80;
    private int minHeight = 60;

    private ImageView imageView;
    private Button btn;
    private TextView textView1;
    private SeekBar seekBar1, seekBar2;

    private Matrix matrix = new Matrix();
    private int newWidth = 80;
    private int newHeight = 60;

    private Intent intent;
    private String path;
    private Bitmap bitmap, newBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_adjust);

        intent = getIntent();
        path = intent.getStringExtra("path");
        bitmap = BitmapFactory.decodeFile(path);

        imageView = (ImageView) findViewById(R.id.imageview);

        imageView.setImageBitmap(bitmap);

        seekBar1 = (SeekBar) findViewById(R.id.seekbar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekbar2);
        textView1 = (TextView) findViewById(R.id.textview1);
        btn = (Button) findViewById(R.id.saveButton);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        btn.setOnClickListener(this);

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);

        // 一个结构描述的一般信息显示，比如大小、密度、和字体缩放
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        seekBar1.setMax(displayMetrics.widthPixels - minWidth);
        seekBar2.setMax(displayMetrics.heightPixels - minHeight - 500);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seekbar1:
                newWidth = i + minWidth;
                imageView.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                textView1.setText("图像宽度：" + newWidth + " 图像高度：" + newHeight);
                break;
            case R.id.seekbar2:
                newHeight = i + minHeight;
                imageView.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
                textView1.setText("图像宽度：" + newWidth + " 图像高度：" + newHeight);
                break;
        }
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
                newBitmap =((BitmapDrawable) ((ImageView) imageView).getDrawable()).getBitmap();
                ImageHelper.saveBitmapFile(newBitmap, this);
                break;
        }
    }
}