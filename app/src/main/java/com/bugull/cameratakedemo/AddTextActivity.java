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

    private Button btn;
    private SeekBar sb_h, sb_v, sb_c, sb_s;
    private ImageView iv_addText;
    private EditText ed_addText;

    private Intent intent;
    private String path, mText;
    private Bitmap bitmap, newBitmap;

    private int color = 0, paddingLeft = 0, paddingTop = 0, mSize = 50;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);

        btn = (Button) findViewById(R.id.saveButton);
        sb_h = (SeekBar) findViewById(R.id.sbHorizontal);
        sb_v = (SeekBar) findViewById(R.id.sbVertical);
        sb_c = (SeekBar) findViewById(R.id.sbColor);
        sb_s = (SeekBar) findViewById(R.id.sbSize);
        iv_addText = (ImageView) findViewById(R.id.ivAddText);
        ed_addText = (EditText) findViewById(R.id.edAdd);

        intent = getIntent();
        path = intent.getStringExtra("path");
        bitmap = BitmapFactory.decodeFile(path);
        newBitmap = bitmap;
        iv_addText.setImageBitmap(bitmap);

        final int WIDTH_MAX = bitmap.getWidth();
        final int HEIGHT_MAX = bitmap.getHeight();
        final int COLOR_MAX = 256 * 6 - 1;
        final int COLOR_MID = 256 * 3;

        final int SIZE_MAX = 200;
        final int SIZE_PROGRESS = 50;

        sb_h.setMax(WIDTH_MAX);
        sb_v.setMax(HEIGHT_MAX);
        sb_c.setMax(COLOR_MAX);
        sb_c.setProgress(COLOR_MID);
        sb_s.setMax(SIZE_MAX);
        sb_s.setProgress(SIZE_PROGRESS);

        btn.setOnClickListener(this);
        sb_h.setOnSeekBarChangeListener(this);
        sb_v.setOnSeekBarChangeListener(this);
        sb_c.setOnSeekBarChangeListener(this);
        sb_s.setOnSeekBarChangeListener(this);

        iv_addText.setImageBitmap(bitmap);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean flag) {

        switch (seekBar.getId()) {
            case R.id.sbHorizontal:
                paddingLeft = i;
                break;
            case R.id.sbVertical:
                paddingTop = i;
                break;
            case R.id.sbColor:
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
                mSize = i;
                break;
        }

        mText = (ed_addText.getText().toString()).equals("")?"请输入文字":(ed_addText.getText().toString());
        newBitmap = ImageHelper.drawTextToLeftTop(this, bitmap, mText, mSize, color, paddingLeft, paddingTop);
        iv_addText.setImageBitmap(newBitmap);

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
