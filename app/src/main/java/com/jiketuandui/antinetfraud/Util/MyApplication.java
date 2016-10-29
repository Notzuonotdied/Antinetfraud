package com.jiketuandui.antinetfraud.Util;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Notzuonotdied on 2016/8/6.
 *
 * 全局变量类
 */
public class MyApplication extends Application {
    private int myScreenWidth;
    private int myScreenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 初始化Fresco类
         * */
        Fresco.initialize(this);
        /**
         * 初始化获取屏幕的宽度和高度
         * */
        initScreenWidth();
        /**
         * 初始化Tag标签列表
         * */
        initTagsList();
    }

    private void initTagsList() {

    }

    /**
     * 获取屏幕的参数，宽度和高度
     */
    private void initScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)
                this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        myScreenHeight = metrics.heightPixels;
        myScreenWidth = metrics.widthPixels;
    }


    public int getMyScreenWidth() {
        return myScreenWidth;
    }

    public void setMyScreenWidth(int myScreenWidth) {
        this.myScreenWidth = myScreenWidth;
    }

    public int getMyScreenHeight() {
        return myScreenHeight;
    }

    public void setMyScreenHeight(int myScreenHeight) {
        this.myScreenHeight = myScreenHeight;
    }
}
