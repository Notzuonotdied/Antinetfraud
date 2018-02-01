package com.jiketuandui.antinetfraud.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 主页的四个Fragment的对应的ViewPager
 * 不可以滑动，但是可以setCurrentItem的ViewPager。
 * 至于什么要设置他不能滑动,是因为多个滑动时间会导致滑动功能冲突并且失效
 *
 * @author Notzuonotdied
 * @date 2016/7/31
 */
public class ContentViewPager extends ViewPager {
    /**
     * 去子控件,如果没有子控件则不触发 触摸事件
     */
    public static boolean GO_TOUTH_CHILD = true;

    public ContentViewPager(Context context) {
        super(context);
    }

    public ContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 拦截方法,
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !GO_TOUTH_CHILD && super.onInterceptTouchEvent(ev);

    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    /**
     * 触摸方法 传给父控件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !GO_TOUTH_CHILD && super.onTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

//    @Override
//    public void setCurrentItem(int item) {
//        //表示切换的时候，不需要切换时间。
//        super.setCurrentItem(item, false);
//    }
}
