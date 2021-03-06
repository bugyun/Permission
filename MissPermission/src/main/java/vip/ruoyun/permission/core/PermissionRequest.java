package vip.ruoyun.permission.core;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fanpu on 2017/8/4.
 * 权限请求
 */
public class PermissionRequest {

    //
    private Set<String> permissionList = new HashSet<>();//请求的权限
    private Set<String> agreePermissionList = new HashSet<>();//同意的权限
    private Set<String> deniedPermissionList = new HashSet<>();//拒绝的权限

    private boolean alwaysDenied = false;//是否总是拒绝
    private boolean isOver23;//是不是棉花糖，大于：true  小于 false Build.VERSION_CODES.M

    //
    private int requestCode = 9898;
    private PermissionListener permissionListener;
    private final WeakReference<Activity> activityWeakReference;

    PermissionRequest(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    //开始
    void start(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
        checkPermission();
    }

    public void next() {
        requestPermission();
    }

    private void checkPermission() {
        if (permissionList.isEmpty()) {
            permissionListener.onFailure(new PermissionException("请求权限为空"));
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOver23 = true;
            for (String permission : permissionList) {
                if (activityWeakReference.get() == null) {
                    permissionListener.onFailure(new PermissionException("activity 为空"));
                    return;
                }
                //ContextCompat
                int checkSelfPermission = ActivityCompat.checkSelfPermission(activityWeakReference.get(), permission);
                if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {//如果同意
                    agreePermissionList.add(permission);
                } else {//如果拒绝
                    deniedPermissionList.add(permission);
                }
            }
            switch (permissionListener.onChecked(this)) {
                case MissPermission.NEXT_STEP:
                    next();
                    break;
                case MissPermission.STOP_STEP:
                case MissPermission.WAIT_STEP:
                default:
                    break;
            }
        } else {
            isOver23 = false;
            /*
             * 检测 23 版本以下 权限的方法 PermissionChecker.checkSelfPermission
             * PERMISSION_GRANTED: 已授权
             * PERMISSION_DENIED: 没有被授权
             * PERMISSION_DENIED_APP_OP: 没有被授权
             */
            for (String permission : permissionList) {
                if (activityWeakReference.get() == null) {
                    permissionListener.onFailure(new PermissionException("activity 为空"));
                    return;
                }
                int checkSelfPermission = PermissionChecker.checkSelfPermission(activityWeakReference.get(), permission);
                if (checkSelfPermission == PermissionChecker.PERMISSION_GRANTED) {//已授权
                    agreePermissionList.add(permission);
                } else {//未授权
                    deniedPermissionList.add(permission);
                }
            }
            if (permissionList.size() == agreePermissionList.size()) {
                permissionListener.onSuccess(this); //所有权限都通过
            } else {
                permissionListener.onDenied(this);
            }
        }
    }

    private void requestPermission() {
        if (permissionList.size() == agreePermissionList.size()) {//判断集合
            //所有权限都通过
            permissionListener.onSuccess(this);
        } else {
            requestPermissionsAgain();
        }
    }

    public void requestPermissionsAgain() {
        if (activityWeakReference.get() == null) {
            permissionListener.onFailure(new PermissionException("activity 为空"));
            return;
        }
//        ActivityCompat.requestPermissions(activityWeakReference.get(), permissionLists.toArray(new String[permissionLists.size()]), requestCode);
        ActivityCompat.requestPermissions(activityWeakReference.get(), deniedPermissionList.toArray(new String[0]), requestCode);
    }


    public interface PermissionListener {
        /**
         * @param request
         * @return NEXT_STEP：直接下一步，不用等待
         * STOP_STEP：直接停止，不执行下一步
         * PAUSE_STEP：等待，等待命令唤起下一步
         */
        int onChecked(PermissionRequest request);//检查结束

        /**
         * @param request
         */
        void onDenied(PermissionRequest request);

        void onSuccess(PermissionRequest request);//权限完成

        void onFailure(PermissionException exception);//失败
    }

    /**
     * @param permissions  请求的权限列表
     * @param grantResults 请求权限的结果列表
     */
    void onRequestPermissionsResult(String[] permissions, int[] grantResults) {
        if (grantResults.length > 0) {
            Set<String> deniedList = new HashSet<>();
            // 遍历所有申请的权限，把被拒绝的权限放入集合
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult != PackageManager.PERMISSION_GRANTED) {//没有授予权限
                    //如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activityWeakReference.get(), permissions[i])) {
                        alwaysDenied = true;
                    }
                    deniedList.add(permissions[i]);
                } else {
                    agreePermissionList.add(permissions[i]);
                }
            }
            deniedPermissionList = deniedList;
            if (!deniedList.isEmpty()) {
                permissionListener.onDenied(this);
            } else {
                permissionListener.onSuccess(this);
            }
        }
    }

    //build 方法 get set

    void addPermission(String permission) {
        permissionList.add(permission);
    }

    void addPermissions(List<String> permissions) {
        permissionList.addAll(permissions);
    }

    void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    int getRequestCode() {
        return requestCode;
    }

    //对外的 get set
    public boolean isOver23() {
        return isOver23;
    }

    public Context getContext() {
        return activityWeakReference.get();
    }

    public Set<String> getPermissionList() {
        return permissionList;
    }

    public boolean isAlwaysDenied() {
        return alwaysDenied;
    }

    public Set<String> getAgreePermissionList() {
        return agreePermissionList;
    }

    public Set<String> getDeniedPermissionList() {
        return deniedPermissionList;
    }
}
