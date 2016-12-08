package com.locker.lizhe.fancylocker.core;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.locker.lizhe.fancylocker.LockerApplication;
import com.locker.lizhe.fancylocker.cover.FloatWindowController;
import com.locker.lizhe.fancylocker.utils.LockUtils;

@SuppressWarnings("deprecation")
public class LockService extends Service{

    private static final String KEYGUARD_LOCK_NAME = "KeyguardLock";
    private static final String START_ACTION = "com.locker.lizhe.fancylocker.core.LockService";

    private KeyguardManager.KeyguardLock keyguardLock;

    private boolean setForeground;

    @Override
    public void onCreate() {
        super.onCreate();

        FloatWindowController.getInstance().start();
//        SystemSettingMgr.getInstance().setBrightnessController(FloatWindowController.getInstance());

        KeyguardManager keyguardMgr = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = keyguardMgr.newKeyguardLock(KEYGUARD_LOCK_NAME);
        try {
            keyguardLock.disableKeyguard();
        } catch (Exception e) {
            keyguardLock = null;
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        FloatWindowController.getInstance().stop();

        if (LockUtils.canDrawOverlays()) {
            startService(getIntent());
        } else {
            if (keyguardLock != null) {
                keyguardLock.reenableKeyguard();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!setForeground) {
            startForeground(LockNotificationMgr.NOTIFICATION_ID_FOREGROUND, LockNotificationMgr.getInstance().getForegroundNotification());
            startService(new Intent(this, LockServicePhantom.class));
            setForeground = true;
        }

        FloatWindowController.getInstance().showLockScreen();
        return START_STICKY;
    }

    public static Intent getIntent() {
        Intent intent = new Intent();
        intent.setAction(START_ACTION);
        intent.setPackage(LockerApplication.getContext().getPackageName());
        return intent;
    }
}
