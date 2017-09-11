package com.bugull.cameratakedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class AdjustActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private int width = 80;
    private int height = 60;

//    控件
    private ImageView pImageView;
    private Button pButton;
    private TextView pTextView;
    private SeekBar seekbar_h, seekbar_v;

//    新的高宽初始值
    private int newW = 80;
    private int newH = 60;

//    用于接受数据
    private Intent pIntent;
    private String path;
    private Bitmap bitmap, newBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lashen);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            将图片保存到实现设置好的路径下
            case R.id.sbtn:
                newBitmap =((BitmapDrawable) ((ImageView) pImageView).getDrawable()).getBitmap();
                HandleFunctions.saveImage(newBitmap, this);
                break;
        }
    }

    private void init() {
        //        接受界面跳转带过来的path字符串，并且将该路径下的图片转换为Bitmap
        pIntent = getIntent();
        path = pIntent.getStringExtra("path");
        bitmap = BitmapFactory.decodeFile(path);

//        绑定各个控件
        pImageView = (ImageView) findViewById(R.id.imageview);
        seekbar_h = (SeekBar) findViewById(R.id.seekbar1);
        seekbar_v = (SeekBar) findViewById(R.id.seekbar2);
        pTextView = (TextView) findViewById(R.id.textview1);
        pButton = (Button) findViewById(R.id.sbtn);
//        给imageView设置图片
        pImageView.setImageBitmap(bitmap);

//        设置ImageView的显示方式，便于拉伸
        pImageView.setScaleType(ImageView.ScaleType.FIT_XY);

//        设置监听事件
        pButton.setOnClickListener(this);
        seekbar_h.setOnSeekBarChangeListener(this);
        seekbar_v.setOnSeekBarChangeListener(this);

        // 一个结构描述的一般信息显示，比如大小、密度、和字体缩放
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        seekbar_h.setMax(displayMetrics.widthPixels - width);
        seekbar_v.setMax(displayMetrics.heightPixels - height - 500);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seekbar1:
//                将ImageView的width参数设置为newW
                newW = i + width;
                pImageView.setLayoutParams(new LinearLayout.LayoutParams(newW, newH));
                pTextView.setText("宽度：" + newW + " 高度：" + newH);
                break;
            case R.id.seekbar2:
//                将ImageView的height参数设置为newH
                newH = i + height;
                pImageView.setLayoutParams(new LinearLayout.LayoutParams(newW, newH));
                pTextView.setText("宽度：" + newW + " 高度：" + newH);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}