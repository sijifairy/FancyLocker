package com.locker.lizhe.fancylocker;

import android.app.Application;
import android.content.Context;

import com.locker.lizhe.fancylocker.core.LockNotificationMgr;
import com.locker.lizhe.fancylocker.core.LockService;
import com.locker.lizhe.fancylocker.cover.FloatWindowController;
import com.locker.lizhe.fancylocker.utils.LockUtils;

public class LockerApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        LockNotificationMgr.init(this);
        FloatWindowController.init(this);
        if(LockUtils.canDrawOverlays()){
            startService(LockService.getIntent());
        }
    }

    public static Context getContext() {
        return context;
    }
}
