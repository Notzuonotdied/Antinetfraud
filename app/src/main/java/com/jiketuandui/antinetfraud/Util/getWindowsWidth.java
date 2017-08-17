package com.jiketuandui.antinetfraud.Util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Notzuonotdied on 2016/8/9.
 * 获取屏幕的宽度
 */
public class getWindowsWidth {
    private int mScreenWidth;
    public getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }
}
