package com.locker.lizhe.fancylocker.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import com.locker.lizhe.fancylocker.LockerApplication;

public class LockUtils {

    public static boolean canDrawOverlays() {
        boolean canDrawOverlays = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canDrawOverlays = Settings.canDrawOverlays(LockerApplication.getContext());
        }
        return canDrawOverlays;
    }

    public static boolean canShowLockScreen(Context context) {
        boolean hasAlertWindowPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED;
        return hasAlertWindowPermission && canDrawOverlays();
    }
}
