package com.locker.lizhe.fancylocker.cover;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;

import com.locker.lizhe.fancylocker.R;

import java.util.ArrayList;
import java.util.List;

public class FloatWindowControllerImpl {

    private List<String> startAlarmAction;
    private List<String> terminalAlarmAction;
    private Context context;
    private boolean isAutoUnlocked = false;
    private boolean isCalling = false;

    private LockScreenWindow lockScreenWindow;
    private TelephonyManager telephonyMgr;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        private final static String SYSTEM_DIALOG_REASON_KEY = "reason";
        private final static String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        private boolean alarmAlert;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (null != reason && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    if (lockScreenWindow != null) {
//                        lockScreenWindow.onHomeKeyClicked();
                    }
                    if (isAutoLockState()) {
                        showLockScreen();
                    }
                }
            } else if (startAlarmAction.contains(intent.getAction())) {
                if (lockScreenWindow.isShown()) {
                    hideLockScreen(false, true);
                    alarmAlert = true;
                }
            } else if (terminalAlarmAction.contains(intent.getAction())) {
                if (alarmAlert) {
                    showLockScreen();
                    alarmAlert = false;
                }
            }
        }
    };

    @SuppressLint("InflateParams")
    public FloatWindowControllerImpl(final Context context) {
        this.context = context;

        ScreenStateMgr.getInstance().startTimerTask();
        lockScreenWindow = (LockScreenWindow) LayoutInflater.from(context).inflate(R.layout.lock_screen, null);

        initAlertAction();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        for (String action : startAlarmAction) {
            intentFilter.addAction(action);
        }

        for (String action : terminalAlarmAction) {
            intentFilter.addAction(action);
        }
        context.registerReceiver(broadcastReceiver, intentFilter);

        telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyMgr.listen(new PhoneStateListener() {

            private boolean incomingcall;

            public void onCallStateChanged(int state, String number) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        isCalling = true;
                        if (lockScreenWindow.isShown()) {
                            hideLockScreen(false, true);
                            incomingcall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        isCalling = true;
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        isCalling = false;
                        if (incomingcall) {
                            showLockScreen();
                            incomingcall = false;
                        }
                        break;
                    default:
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void initAlertAction() {
        startAlarmAction = new ArrayList<>();
        terminalAlarmAction = new ArrayList<>();

        startAlarmAction.add("com.android.deskclock.ALARM_ALERT");
        startAlarmAction.add("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT");
        startAlarmAction.add("com.htc.worldclock.ALARM_ALERT");
        startAlarmAction.add("com.sonyericsson.alarm.ALARM_ALERT");
        startAlarmAction.add("zte.com.cn.alarmclock.ALARM_ALERT");
        startAlarmAction.add("com.motorola.blur.alarmclock.ALARM_ALERT");
        startAlarmAction.add("com.lge.alarm.alarmclocknew");
        startAlarmAction.add("com.lge.clock.alarmclock");
        startAlarmAction.add("com.htc.android.ALARM_ALERT");
        startAlarmAction.add("android.intent.action.ALARM_CHANGED");

        terminalAlarmAction.add("com.android.deskclock.ALARM_DISMISS");
        terminalAlarmAction.add("com.android.deskclock.ALARM_DONE");
        terminalAlarmAction.add("com.android.deskclock.ALARM_SNOOZE");
    }

    public void adjustBrightness(float brightness) {
//        lockScreenWindow.adjustBrightness(brightness);
    }

    public void showLockScreen() {
        // If user revoked alert window permission, we just do nothing.
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        isAutoUnlocked = false;
        ScreenStateMgr.getInstance().checkTimerTask();
        lockScreenWindow.show();
        startDismissActivity();
//        TopActivityChecker.getInstance().stopChecking();
    }

    private void startDismissActivity() {
//        Intent intentActivity = new Intent(context, DismissActivity.class);
//        intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intentActivity);
    }

    public void hideLockScreen(boolean autoLock, boolean closeDismissActivity) {
//        HSLog.i("hideLockScreen(), auto lock = " + autoLock);
//        if (autoLock) {
//            isAutoUnlocked = true;
//            TopActivityChecker.getInstance().startChecking();
//        }
//        DismissActivity.hide();
////        if (closeDismissActivity) {
////            DismissActivity.hide();
////        }
//        lockScreenWindow.hide(false);
    }

    public void hideLockScreen(int hideType) {
//        HSLog.i("hideLockScreen(), hideType = " + hideType);
//        boolean autoLock = (hideType & FloatWindowController.HIDE_LOCK_WINDOW_AUTO_LOCK) != 0;
//        if (autoLock) {
//            TopActivityChecker.getInstance().startChecking();
//        }
//
//        DismissActivity.hide();
//        lockScreenWindow.hide((hideType & FloatWindowController.HIDE_LOCK_WINDOW_NO_ANIMATION) != 0);
    }

    public void hideUpSlideLockScreen() {
//        DismissActivity.hide();
//        lockScreenWindow.hide(true);
    }


    public boolean isAutoLockState() {
        return !lockScreenWindow.isShown() && isAutoUnlocked;
    }

    public boolean isLockScreenShown() {
        return lockScreenWindow.isShown();
    }

    public boolean isCalling() {
        return isCalling;
    }

    public void release() {
        context.unregisterReceiver(broadcastReceiver);
    }
}
