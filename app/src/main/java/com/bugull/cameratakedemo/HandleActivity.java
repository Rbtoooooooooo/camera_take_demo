package com.bugull.cameratakedemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HandleActivity extends AppCompatActivity {

    private Button zengqiang;
    private Button lvjing;
    private Button lashen;
    private Button tianjiawenzi;
    private ImageView iv;
    private Context pContext;
    private String path;
    private Bitmap pBitmap;
    private Intent pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chuli);

        init();

    }

    private void init() {
//        上下文问本activity
        pContext = this;

//        绑定
        zengqiang = (Button)findViewById(R.id.zengqiang);
        lvjing = (Button)findViewById(R.id.lvjing);
        lashen = (Button)findViewById(R.id.lashen);
        tianjiawenzi = (Button) findViewById(R.id.tianjiawenzi);
        iv = (ImageView) findViewById(R.id.piv);

//        得到path 并且得到图片，将其显示出来
        pIntent = getIntent();
        path = pIntent.getStringExtra("path");
        pBitmap = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(pBitmap);

//        以下四个带着path进行界面跳转
        zengqiang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent pIntent = new Intent(pContext, EnhanceActivity.class);
                pIntent.putExtra("path", path);
                startActivity(pIntent);
            }
        });

        lvjing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent pIntent = new Intent(pContext, FilterActivity.class);
                pIntent.putExtra("path", path);
                startActivity(pIntent);
            }
        });

        lashen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent pIntent = new Intent(pContext, AdjustActivity.class);
                pIntent.putExtra("path", path);
                startActivity(pIntent);
            }
        });

        tianjiawenzi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent pIntent = new Intent(pContext, AddTextActivity.class);
                pIntent.putExtra("path", path);
                startActivity(pIntent);
            }
        });
    }

}
