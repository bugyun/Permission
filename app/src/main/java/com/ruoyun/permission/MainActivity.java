package com.ruoyun.permission;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import vip.ruoyun.permission.pro.DefaultAction;
import vip.ruoyun.permission.pro.MissPermission;
import vip.ruoyun.permission.pro.PermissionRequest;
import vip.ruoyun.permission.pro.check.SMSChecker;
import vip.ruoyun.permission.pro.ui.AgainRequestDialog;
import vip.ruoyun.permission.pro.ui.AlwaysDeniedDialog;
import vip.ruoyun.permission.pro.ui.RequestPromptDialog;
import vip.ruoyun.screen.ScreenHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    Button buttonCamera;

    Button buttonAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //测试
        buttonCamera = findViewById(R.id.buttonCamera);
        buttonAll = findViewById(R.id.buttonAll);
        buttonCamera.setOnClickListener(this);
        buttonAll.setOnClickListener(this);
        String manufacturer = android.os.Build.MANUFACTURER;
        Log.e("zyh", "制造商" + manufacturer);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCamera:
                MissPermission.with()
//                        .permission(Manifest.permission.SEND_SMS)//
//                        .permission(Manifest.permission.RECEIVE_SMS)//
//                        .permission(Manifest.permission.READ_SMS)//
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION)//
                        .permission(Manifest.permission.CAMERA)//
                        .permission(Manifest.permission.READ_CONTACTS)//
                        .permission(Manifest.permission.WRITE_CALENDAR)//
//                        .permission(Manifest.permission.READ_CALL_LOG)//
                        .permission(Manifest.permission.READ_CONTACTS)//
                        .permission(Manifest.permission.RECORD_AUDIO)//
//                        .permission(Manifest.permission.BODY_SENSORS)//
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)//
                        .action(new DefaultAction() {
                            @Override
                            public void onActivityResult(int resultCode, Intent data) {

                            }

                            //可选，初始化请求弹窗的时候，可以通过此方法设置监听
                            @Override
                            public void onCreateRequestPromptDialog(RequestPromptDialog requestPromptDialog) {
                            }

                            //可选，初始化总是拒绝此权限时的弹窗的时候，可以通过此方法设置监听
                            @Override
                            public void onCreateAlwaysDeniedDialog(AlwaysDeniedDialog getAlwaysDeniedDialog) {
                            }

                            //可选，初始化再次请求权限的弹窗的时候，可以通过此方法设置监听
                            @Override
                            public void onCreateAgainRequestDialog(AgainRequestDialog getAgainRequestDialog) {
                            }
                        })
                        .msg("为了您正常使用应用,需要以下权限")
                        .title("亲爱的用户")
                        .prompt(true)
                        .style(R.style.MissPermissionDefaultNormalStyle)
                        .check(new PermissionRequest.PermissionListener() {
                            @Override
                            public void onSuccess(PermissionRequest request) {
                                Toast.makeText(MainActivity.this, "获取权限成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(PermissionRequest request) {
//                                Toast.makeText(MainActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
                                Log.e("zyh", "onFailure : " + request.getException().getMessage());
                            }
                        });

                break;
            case R.id.buttonAll:
                if (MissPermission.check(this, SMSChecker.NEED_PERMISSION)) {
                    Log.e(TAG, "有权限");
                } else {
                    Log.e(TAG, "没有权限");
                }

//                if (MissPermission.realCheck(this, Manifest.permission.READ_SMS)) {
//                    Log.e(TAG, "有短信权限");
//
//                } else {
//                    Log.e(TAG, "没有短信权限");
//                }
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    public Resources getResources() {
        return ScreenHelper.applyAdapt(super.getResources(), 375f, ScreenHelper.WIDTH_DP);
    }

}
