package com.locker.lizhe.fancylocker.cover;

import android.content.Context;

public class FloatWindowController {

    public final static int HIDE_LOCK_WINDOW_NONE = 0;
    public final static int HIDE_LOCK_WINDOW_AUTO_LOCK = 1;
    public final static int HIDE_LOCK_WINDOW_DISMISS_ACTIVITY = 2;
    public final static int HIDE_LOCK_WINDOW_NO_ANIMATION = 4;

    private static FloatWindowController instance;

    private Context context;

    private FloatWindowControllerImpl floatWindowControllerImpl;

    public static void init(Context context) {
        if (instance == null) {
            instance = new FloatWindowController(context);
        }
    }

    public static FloatWindowController getInstance() {
        return instance;
    }

    private FloatWindowController(Context context) {
        this.context = context;
    }

    public synchronized void start() {
        if (null == floatWindowControllerImpl) {
            floatWindowControllerImpl = new FloatWindowControllerImpl(context);
        }
    }

    public synchronized void stop() {
        if (null != floatWindowControllerImpl) {
            floatWindowControllerImpl.release();
            floatWindowControllerImpl = null;
        }
    }

    public void adjustBrightness(float brightness) {
        if (null != floatWindowControllerImpl) {
            floatWindowControllerImpl.adjustBrightness(brightness);
        }
    }

    public void showLockScreen() {
        if (null != floatWindowControllerImpl) {
            floatWindowControllerImpl.showLockScreen();
        }
    }

    public void hideLockScreen(boolean autoLock) {
        hideLockScreen(autoLock ? HIDE_LOCK_WINDOW_AUTO_LOCK : HIDE_LOCK_WINDOW_NONE);
    }

    public void hideLockScreen(boolean autoLock, boolean closeDismissActivty) {
        int type = autoLock ? HIDE_LOCK_WINDOW_AUTO_LOCK : HIDE_LOCK_WINDOW_NONE;
        type |= closeDismissActivty ? HIDE_LOCK_WINDOW_DISMISS_ACTIVITY : HIDE_LOCK_WINDOW_NONE;
        hideLockScreen(type);
    }

    public void hideLockScreen(int closeType) {
        if (null != floatWindowControllerImpl) {
            floatWindowControllerImpl.hideLockScreen(closeType);
        }
    }

    public void hideUpSlideLockScreen() {
        if (null != floatWindowControllerImpl) {
            floatWindowControllerImpl.hideUpSlideLockScreen();
        }
    }

    public boolean isAutoLockState() {
        if (null != floatWindowControllerImpl) {
            return floatWindowControllerImpl.isAutoLockState();
        }
        return false;
    }

    public boolean isLockScreenShown() {
        if (null == floatWindowControllerImpl) {
            return false;
        }
        return floatWindowControllerImpl.isLockScreenShown();
    }

    public boolean isCalling() {
        if (null == floatWindowControllerImpl) {
            return false;
        }
        return floatWindowControllerImpl.isCalling();
    }
}
