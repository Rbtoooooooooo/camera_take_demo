package com.bugull.cameratakedemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HandleActivity extends AppCompatActivity {

    private Button enhanceButton;
    private Button filterButton;
    private Button adjustButton;
    private Button addTextButton;
    private ImageView iv_handle;
    private TextView tv_main;
    private Context mContext;
    private String path;
    private Bitmap bmp;
    private Intent mIntent;

    private TextView tv_handle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle);
        mContext = this;

        enhanceButton = (Button)findViewById(R.id.enhanceButton);
        filterButton = (Button)findViewById(R.id.filterButton);
        adjustButton = (Button)findViewById(R.id.adjustButton);
        addTextButton = (Button) findViewById(R.id.addTextButton);
        iv_handle = (ImageView) findViewById(R.id.handleImageView);

        mIntent = getIntent();
        path = mIntent.getStringExtra("path");
        bmp = BitmapFactory.decodeFile(path);
        iv_handle.setImageBitmap(bmp);

        enhanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EnhanceActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FilterActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        adjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AdjustActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        addTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddTextActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });
    }

}
