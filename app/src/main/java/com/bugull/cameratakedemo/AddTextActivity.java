package com.bugull.cameratakedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;


/**
 * Created by luopu on 2017/7/30.
 */

public class AddTextActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{

//    用于控制控件
    private Button pButton;
    private SeekBar seekbar_h, seekbar_v, seekbar_c, seekbar_s;
    private ImageView pImageview;
    private EditText pEditText;

//    用于接受界面跳转带过来得数据
    private Intent intent;
    private String path, pText;
    private Bitmap pBitmap, newBitmap;

    private int color = 0, pLeft = 0, pTop = 0, pSize = 50;

//    给各个seekbar声明一个最大值
    final int WIDTH = pBitmap.getWidth();
    final int HEIGHT = pBitmap.getHeight();
    final int MAX = 256 * 6 - 1;
    final int MID = 256 * 3;

    final int MAX_SIZE = 200;
    final int PROGRESS = 50;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        将tianjaiwenzi.xml设置该activity的布局
        setContentView(R.layout.tianjiawenzi);

        init();

    }

    private void init() {
        //        绑定该布局的各个控件
        pButton = (Button) findViewById(R.id.sbtn);
        seekbar_h = (SeekBar) findViewById(R.id.sbHorizontal);
        seekbar_v = (SeekBar) findViewById(R.id.sbVertical);
        seekbar_c = (SeekBar) findViewById(R.id.sbColor);
        seekbar_s = (SeekBar) findViewById(R.id.sbSize);
        pImageview = (ImageView) findViewById(R.id.ivAddText);
        pEditText = (EditText) findViewById(R.id.edAdd);

//        接受从上一个界面传过来的path字符串
        intent = getIntent();
        path = intent.getStringExtra("path");

//        把path路径下的图片转为Bitmap类型的图片
        pBitmap = BitmapFactory.decodeFile(path);
        newBitmap = pBitmap;
        pImageview.setImageBitmap(pBitmap);



//        给seekbar设置最大值，初始值
        seekbar_h.setMax(WIDTH);
        seekbar_v.setMax(HEIGHT);
        seekbar_c.setMax(MAX);
        seekbar_c.setProgress(MID);
        seekbar_s.setMax(MAX_SIZE);
        seekbar_s.setProgress(PROGRESS);

//        设置监听事件
        pButton.setOnClickListener(this);
        seekbar_h.setOnSeekBarChangeListener(this);
        seekbar_v.setOnSeekBarChangeListener(this);
        seekbar_c.setOnSeekBarChangeListener(this);
        seekbar_s.setOnSeekBarChangeListener(this);

        pImageview.setImageBitmap(pBitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            点击保存按钮保存图片
            case R.id.sbtn:
                HandleFunctions.saveImage(newBitmap, this);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean flag) {

//        根据被滑动的seekbar的id来进行操作
        switch (seekBar.getId()) {
            case R.id.sbHorizontal:
//                要向右移动
                pLeft = i;
                break;
            case R.id.sbVertical:
//                要向下移动
                pTop = i;
                break;
            case R.id.sbColor:
//                要改变字体颜色
                int r=0, g=0, b=0;
                if (i < 256) {
                    r = 255;
                    g = i % 256;
                } else if (i < 256 * 2) {
                    g = 256;
                    r = 256 - i % 256;
                } else if (i < 256 * 3) {
                    r = 0;
                    g = 255;
                    b = i % 256;
                } else if (i < 256 * 4) {
                    b = 255;
                    g = 256 - i % 256;
                } else if (i < 256 * 5) {
                    b = 255;
                    r = i % 256;
                } else if (i < 256 * 6) {
                    r = 255;
                    b = 256 - i % 256;
                }
                color = -(r*256*256 + g*256 + b);
                break;
            case R.id.sbSize:
//                要改变字体大小
                pSize = i;
                break;
        }
//        从EditText得到需要添加的文字
        pText = (pEditText.getText().toString()).equals("")?"请输入文字":(pEditText.getText().toString());
//        将需要进行的改变和文字部署到图片上去
        newBitmap = HandleFunctions.addText1(this, pBitmap, pText, pSize, color, pLeft, pTop);
        pImageview.setImageBitmap(newBitmap);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
