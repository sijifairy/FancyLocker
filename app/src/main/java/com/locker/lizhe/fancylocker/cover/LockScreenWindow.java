package com.locker.lizhe.fancylocker.cover;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.locker.lizhe.fancylocker.utils.LockUtils;
import com.locker.lizhe.fancylocker.utils.Utils;
import com.locker.lizhe.fancylocker.utils.WindowParamUtils;

public class LockScreenWindow extends FrameLayout {

    private WindowManager windowMgr;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager.LayoutParams emptyParams;
    private View emptyView;
    private boolean addedToWindowMgr;
    private int paddingBottom;

    public LockScreenWindow(Context context) {
        this(context, null);
    }

    public LockScreenWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockScreenWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        windowMgr = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        emptyView = new View(getContext());
        emptyParams = WindowParamUtils.getEmptyParams(getContext());
        layoutParams = WindowParamUtils.getLockerParams(getContext());
        paddingBottom = Utils.getNavigationBarHeight(getContext());
    }

    public void show() {
        if (!addedToWindowMgr && LockUtils.canShowLockScreen(getContext())) {
            windowMgr.addView(this, layoutParams);
            addedToWindowMgr = true;
        }
    }

    public void hide() {
        if (!addedToWindowMgr) {
            return;
        }
        windowMgr.removeView(this);
        try {
            if (LockUtils.canShowLockScreen(getContext())) {
                windowMgr.addView(emptyView, emptyParams);
                windowMgr.removeView(emptyView);
            }
        } catch (IllegalStateException exception) {
            //window operation is not sync, this exception would occur.
            exception.printStackTrace();
        }
        addedToWindowMgr = false;
    }
}
