package vip.ruoyun.permission.helper.check;

import android.Manifest;
import android.content.Context;

import vip.ruoyun.permission.helper.core.IChecker;
import vip.ruoyun.permission.helper.core.MissHelperConfiguration;

/**
 * Created by ruoyun on 2019-07-05.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class MicrophoneChecker implements IChecker {

    public final String[] NEED_PERMISSION = {
            Manifest.permission.RECORD_AUDIO,//
    };

    @Override
    public boolean isCheckEnable(Context context, MissHelperConfiguration configuration) {
        return false;
    }
}