package com.bugull.cameratakedemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class PActivity extends AppCompatActivity {

    public static final int GRANTED = 0; // 权限授权
    public static final int DENIED = 1; // 权限拒绝

    private static final int PERMISSION = 0; // 系统权限管理页面的参数
    private static final String EXTRA =
            "me.chunyu.clwang.permission.extra_permission"; // 权限参数
    private static final String PACKAGE = "package:"; // 方案

    private CheckPermissions m; // 权限检测器
    private boolean isRequire; // 是否需要系统权限检测

    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity pActivity, int requestCode, String... permissions) {
        Intent intent = new Intent(pActivity, PActivity.class);
        intent.putExtra(EXTRA, permissions);
        ActivityCompat.startActivityForResult(pActivity, intent, requestCode, null);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA)) {
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        }
        setContentView(R.layout.activity_permissions);

        m = new CheckPermissions(this);
        isRequire = true;
    }

    @Override protected void onResume() {
        super.onResume();
        if (isRequire) {
            String[] permissions = getPermissions();
            if (m.lacksPermissions(permissions)) {
                requestPermissions(permissions); // 请求权限
            } else {
                allPermissionsGranted(); // 全部权限都已获取
            }
        } else {
            isRequire = true;
        }
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA);
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
        setResult(GRANTED);
        finish();
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION && hasAllPermissionsGranted(grantResults)) {
            isRequire = true;
            allPermissionsGranted();
        } else {
            isRequire = false;
            showMissingPermissionDialog();
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PActivity.this);
        builder.setTitle("帮助");
        builder.setMessage("当前应用缺少必要权限");

        // 拒绝, 退出应用
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                setResult(DENIED);
                finish();
            }
        });

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE + getPackageName()));
        startActivity(intent);
    }
}
