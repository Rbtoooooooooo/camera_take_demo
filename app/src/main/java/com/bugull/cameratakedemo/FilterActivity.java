package com.bugull.cameratakedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.bugull.cameratakedemo.R.id.huaijiuButton;

/**
 * Created by luopu on 2017/7/4.
 */

public class FilterActivity extends Activity implements View.OnClickListener{

    static private Intent mIntent;
    static private ImageView iv_filter;
    static private Button btn_huaijiu, btn_fudiao, btn_guangzhao, btn_heibai, btn_dipian, btn_save;
    static private String path;
    static private Bitmap bmp, newbmp;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lvjing);

        iv_filter = (ImageView) findViewById(R.id.filterImageView);
        btn_huaijiu = (Button) findViewById(huaijiuButton);
        btn_fudiao = (Button) findViewById(R.id.fudiaoButton);
        btn_guangzhao = (Button) findViewById(R.id.guangzhaoButton);
        btn_heibai = (Button) findViewById(R.id.heibaiButton);
        btn_dipian = (Button) findViewById(R.id.dipianButton);
        btn_save = (Button) findViewById(R.id.sbtn);

        btn_huaijiu.setOnClickListener(this);
        btn_fudiao.setOnClickListener(this);
        btn_guangzhao.setOnClickListener(this);
        btn_heibai.setOnClickListener(this);
        btn_dipian.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        mIntent = getIntent();
        path = mIntent.getStringExtra("path");
        bmp = BitmapFactory.decodeFile(path);
        newbmp = BitmapFactory.decodeFile(path);
        iv_filter.setImageBitmap(bmp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.huaijiuButton:
                newbmp = HandleFunctions.pikaGoGoGo(bmp, HandleFunctions.Lvjing.HUAIJIU);
                break;
            case R.id.fudiaoButton:
                newbmp = HandleFunctions.pikaGoGoGo(bmp, HandleFunctions.Lvjing.FUDIAO);
                break;
            case R.id.guangzhaoButton:
                // 未完成 !!!!!!!!!!!
                newbmp = HandleFunctions.pikaGoGoGo(bmp, HandleFunctions.Lvjing.JINGXIANG);;
                break;
            case R.id.heibaiButton:
                newbmp = HandleFunctions.pikaGoGoGo(bmp, HandleFunctions.Lvjing.HEIBAI);
                break;
            case R.id.dipianButton:
                newbmp = HandleFunctions.pikaGoGoGo(bmp, HandleFunctions.Lvjing.DIPIAN);
                break;
            case R.id.sbtn:
                HandleFunctions.saveImage(newbmp, this);
                break;
        }
        iv_filter.setImageBitmap(newbmp);
    }
}
