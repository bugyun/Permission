package vip.ruoyun.permission.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;

import java.util.Arrays;
import java.util.List;

public class MissPermissionHelper {

    private static class SingletonHolder {
        private static final MissPermissionHelper INSTANCE = new MissPermissionHelper();
    }

    private static MissPermissionHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private MissPermissionHelper() {
    }

    public static Builder with(FragmentActivity activity) {
        return new Builder(activity);
    }

    public static Builder with(Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static class Builder {

        private PermissionRequest request;
        private IAction baseAction = new DefaultAction();

        private Builder(FragmentActivity activity) {
            request = new PermissionRequest(activity);
        }

        public Builder addPermission(String permission) {
            request.addPermission(permission);
            return this;
        }

        public Builder addPermissions(List<String> permissions) {
            request.addPermissions(permissions);
            return this;
        }

        public Builder addPermissions(String[] permissions) {
            request.addPermissions(Arrays.asList(permissions));
            return this;
        }

        public void checkPermission(PermissionRequest.PermissionListener listener) {
            request.start(listener);
        }

        public Builder showprompt(boolean showprompt) {
//            this.showprompt = showprompt;
            return this;
        }

        public Builder style(String title) {
//            this.title = title;
            return this;
        }

        public Builder msg(String msg) {
//            this.msg = msg;
            return this;
        }

        public Builder mFilterColor(int mFilterColor) {
//            this.mFilterColor = mFilterColor;
            return this;
        }

        public Builder mStyleResId(int mStyleResId) {
//            this.mStyleResId = mStyleResId;
            return this;
        }

        public Builder ischeck(boolean ischeck) {
//            this.ischeck = ischeck;
            return this;
        }

        public Builder missPermission(boolean ischeck) {
//            this.ischeck = ischeck;
            return this;
        }

        public void go() {

        }
    }

    /**
     * 简单有没有权限,如果 [] 为空或者 为 null 时，返回有权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean check(Context context, String[] permissions) {
        boolean isHasPermission = true;//检测权限
        if (permissions == null) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    isHasPermission = false;
                    break;
                }
            }
        } else {
            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                    isHasPermission = false;
                    break;
                }
            }
        }
        return isHasPermission;
    }
}
