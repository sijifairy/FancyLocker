package com.locker.lizhe.fancylocker.cover;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import com.locker.lizhe.fancylocker.LockerApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Checking the state of the screen, and save the state whether it is on or off.
 */
public class ScreenStateMgr {

    private ScreenStateMgr() {
    }

    public static ScreenStateMgr getInstance() {
        return ScreenStateHolder.instance;
    }

    private static class ScreenStateHolder {
        private static final ScreenStateMgr instance = new ScreenStateMgr();
    }

    public final static String ACTION_SCREEN_ON = "ACTION_SCREEN_ON";
    public final static String ACTION_SCREEN_OFF = "ACTION_SCREEN_OFF";

    private boolean isScreenOn;
    private ScheduledExecutorService scheduledThreadPool;
    private long lastActivateTime;
    private final static int MSG_SCREEN_ON = 1;
    private final static int MSG_SCREEN_OFF = 2;

    /**
     * Send ACTION_SCREEN_ON when screen state changes from off to on.
     * Send ACTION_SCREEN_OFF when screen state changes from on to off.
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCREEN_ON:
//                    HSGlobalNotificationCenter.sendNotification(ACTION_SCREEN_ON);
                    break;
                case MSG_SCREEN_OFF:
//                    HSGlobalNotificationCenter.sendNotification(ACTION_SCREEN_OFF);
                    if (!FloatWindowController.getInstance().isLockScreenShown()
                            && !FloatWindowController.getInstance().isCalling()) {
                        FloatWindowController.getInstance().showLockScreen();
                    }
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public boolean isScreenOn() {
        return isScreenOn;
    }

    public void startTimerTask() {
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
//        HSLog.i("screen state new");
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                lastActivateTime = System.currentTimeMillis();
                PowerManager pm = (PowerManager) LockerApplication.getContext().getSystemService(Context.POWER_SERVICE);
                if (pm.isScreenOn() && !isScreenOn) {
//                    HSLog.i("screen state from off to on");
                    isScreenOn = true;
                    Message message = new Message();
                    message.what = MSG_SCREEN_ON;
                    handler.sendMessage(message);
                }
                if (!pm.isScreenOn() && isScreenOn) {
//                    HSLog.i("screen state from on to off");
                    isScreenOn = false;
                    Message message = new Message();
                    message.what = MSG_SCREEN_OFF;
                    handler.sendMessage(message);
                }
            }
        }, 10, 200, TimeUnit.MILLISECONDS);
    }

    public void checkTimerTask() {
        long timeInterval = System.currentTimeMillis() - lastActivateTime;
        if (timeInterval > 1000) {
            if (scheduledThreadPool != null) {
                scheduledThreadPool.shutdownNow();
//                HSLog.i("screen state stop");
            }
            startTimerTask();
        }
    }
}
