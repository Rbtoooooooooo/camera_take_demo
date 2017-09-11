package com.bugull.cameratakedemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_1 = 1; //相册选取
    private static final int REQUEST_2 = 2;  //拍照
    private static final int REQUEST_3 = 3;  //剪裁图片
    private static final int REQUEST_4 = 4;  //权限请求
    private CheckPermissions pPC; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private boolean isClick;
    private String pImagePath;
    private Uri pImageUri;
    public static String imageName;
    private int pFlag = 0;

//    控件
    private Button sBtn, b1, b2, b3, b4;
    private ImageView imageView;
    private Bitmap nBitmap = HandleFunctions.whiteBitmap(), bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.haibao);

        init();
    }

    private void init() {
//        权限检查
        pPC = new CheckPermissions(this);

//        控件绑定
        sBtn = (Button) findViewById(R.id.btnSave);
        b1 = (Button) findViewById(R.id.btn1);
        b2 = (Button) findViewById(R.id.btn2);
        b3 = (Button) findViewById(R.id.btn3);
        b4 = (Button) findViewById(R.id.btn4);
        imageView = (ImageView) findViewById(R.id.ivPost);

//        设置监听
        sBtn.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

//        给ImageView设置一张图片
        imageView.setImageBitmap(nBitmap);
    }



    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent pIntent = new Intent(Intent.ACTION_PICK);
        pIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pIntent, REQUEST_1);
    }

    /**
     * 打开系统相机
     */
    private void openCamera() {
        File file = new SaveImage().iconFile();
//        imageName = file.toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(PostActivity.this, "com.bugull.cameratakedemo.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_2);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
//            根据点击的Button的id设置flag值或者保存图片
            case R.id.btnSave: HandleFunctions.saveImage(nBitmap, this); pFlag = 0; break;
            case R.id.btn1: pFlag = 1; break;
            case R.id.btn2: pFlag = 2; break;
            case R.id.btn3: pFlag = 3; break;
            case R.id.btn4: pFlag = 4; break;

        }

        if (pFlag != 0) {
//            权限检查
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (pPC.lacksPermissions(PERMISSIONS)) {
                    startPermissionsActivity();
                } else {
//                    从相册选择图片
                    selectFromAlbum();
                }
            } else {
//                从相册选择图片
                selectFromAlbum();
            }
            isClick = false;
        }

    }

    private void startPermissionsActivity() {
        PActivity.startActivityForResult(this, REQUEST_4,
                PERMISSIONS);
    }


    /**
     * 裁剪
     */
    private void cropPhoto() {
        File pFile = new SaveImage().cropFile();
        pImageUri = Uri.fromFile(pFile);
        imageName = pFile.toString();
        Uri outputUri = Uri.fromFile(pFile);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_1://从相册选择
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_2://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_3://裁剪完成
                try {
                    if (isClick) {

//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pImageUri));
                    } else {
                        bitmap = BitmapFactory.decodeFile(pImagePath);
                    }
                    nBitmap = HandleFunctions.makePost(nBitmap, bitmap, pFlag, this);
                    imageView.setImageBitmap(nBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_4://权限请求
                if (resultCode == PActivity.DENIED) {
                    finish();
                } else {
                    if (isClick) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        pImagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                pImagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                pImagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            pImagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            pImagePath = imageUri.getPath();
        }
        cropPhoto();
    }


    private String getImagePath(Uri uri, String selection) {
        String s = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                s = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return s;
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        pImagePath = getImagePath(imageUri, null);
        cropPhoto();
    }


}
