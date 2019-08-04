package vip.ruoyun.permission.helper.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import vip.ruoyun.helper.avoid.AvoidOnResultHelper;
import vip.ruoyun.permission.helper.R;

/**
 * Created by fanpu on 2017/6/7.
 */

public class DialogUtil {

    public static void showPermissionManagerDialog(final FragmentActivity context, String str) {
        new AlertDialog.Builder(context).setTitle("获取" + str + "权限被禁用").setMessage("请在 设置-应用管理-" + context.getString(R.string.app_name) + "-权限管理 (将" + str + "权限打开)").setNegativeButton("取消", null).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                AvoidOnResultHelper.startActivityForResult(context, intent, new AvoidOnResultHelper.ActivityCallback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        Log.e("DialogUtil", "返回值为" + resultCode);
                    }
                });
                //                JumPermissionManagement.GoToSetting(context);
            }
        }).show();
    }

    public static void showLocServiceDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("手机未开启位置服务").setMessage("请在 设置-系统安全-位置信息 (将位置服务打开))").setNegativeButton("取消", null).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    intent.setAction(Settings.ACTION_SETTINGS);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).show();
    }


}
