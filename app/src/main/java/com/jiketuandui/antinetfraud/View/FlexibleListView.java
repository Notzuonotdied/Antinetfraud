package com.jiketuandui.antinetfraud.View;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;

/**
 * Created by Notzuonotdied on 2016/9/7.
 * 自定义ListView实现阻尼效果
 */
public class FlexibleListView extends ListView {

    /**
     * 头部View
     */
    private View mHeaderView;
    /**
     * mMaxScrollHeight表示HeaderView的最大滚动距离，
     * 当HeaderView的滚动距离超过此值我们就要设置菜单栏
     * 不透明否则就更改透明度。
     */
    private int mMaxScrollHeight;

    public FlexibleListView(Context context) {
        super(context);
    }

    public FlexibleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlexibleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public FlexibleListView(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        // 获取头部View
        if (mHeaderView == null) {
            mHeaderView = v;
            mMaxScrollHeight = mHeaderView.getLayoutParams().height;
        }
        super.addHeaderView(v, data, isSelectable);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                   int maxOverScrollY, boolean isTouchEvent) {
        if (null != mHeaderView) {
            if (isTouchEvent && deltaY < 0) {
                mHeaderView.getLayoutParams().height += Math.abs(deltaY / 3.0);
                mHeaderView.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != mHeaderView) {
            int action = ev.getAction();
            if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
                resetHeaderViewHeight();
            }
        }
        return super.onTouchEvent(ev);
    }

    private void resetHeaderViewHeight() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float f = animation.getAnimatedFraction();
                mHeaderView.getLayoutParams().height -= f * (
                        mHeaderView.getLayoutParams().height - mMaxScrollHeight);
                mHeaderView.requestLayout();

            }
        });
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.start();
    }
}
