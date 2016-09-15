package com.jiketuandui.antinetfraud.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiketuandui.antinetfraud.R;

/**
 * Created by Notzuonotdied on 2016/8/9.
 * 这个是自定义的TabPageIndicator
 */
public class MyTabPageIndicator extends LinearLayout {
    /**
     * 三角形的宽度为单个Tab的1/5
     */
    private static final float RADIO_TRIANGLE = 1.0f / 5;
    /**
     * 默认的Tab数量
     */
    private static final int COUNT_DEFAULT_TAB = 4;
    /**
     * tab的数量
     */
    private int mTabVisibleCount = COUNT_DEFAULT_TAB;
    /**
     * 标题正常的时候的颜色
     */
    private static int COLOR_TEXT_NORMAL = 0xaaffffff;
    /**
     * 标题选中的时候的颜色
     */
    private static int COLOR_TEXT_HIGHLIGHTCOLOR = 0xffffffff;
    /**
     * 单位是sp
     * */
    private int IndicatorTextSize = 14;
    /**
     * 绘制三角形的画笔
     */
    private Paint mPaint;
    /**
     * 使用path构成一个三角形
     */
    private Path mPath;
    /**
     * 设置三角形的宽度
     */
    private int mTriangleWidth;
    /**
     * 获取屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 设置三角形的最大宽度
     */
    private int DIMENSION_TRIANGEL_WIDTH;
    /**
     * 初始时，三角形指示器的偏移量
     */
    private int mInitTranslationX;
    /**
     * 手指滑动的时候的偏移量
     */
    private float mTranslationX;
    /**
     * 设置ViewPager
     */
    private ViewPager mViewPager;
    /**
     * PagerAdapter adapter
     */
    private PagerAdapter mPagerAdapter;
    // 对外的ViewPager的回调接口
    private PageChangeListener onPageChangeListener;
    /**
     * 响应事件
     */
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        /**
         * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用
         * 
         * @param position 当前页面，及你点击滑动的页面
         * @param positionOffset 当前页面偏移的百分比
         * @param positionOffsetPixels 当前页面偏移的像素的位置
         * */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 滚动
            onScroll(position, positionOffset);
            // 回调
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override// 当被选中的时候
        public void onPageSelected(int position) {
            // 设置字体高亮
            resetTextViewColor();
            highLightTextView(position);
            // 回调
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(position);
            }
        }

        @Override// onPageScrollStateChanged的参数 0表示滑动完毕 1表示按下状态 2表示手指抬起状态
        public void onPageScrollStateChanged(int state) {
            // 回调
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    public MyTabPageIndicator(Context context) {
        this(context, null);
    }

    public MyTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性。
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTabPageIndicator);
        mTabVisibleCount = typedArray.getInt(R.styleable.MyTabPageIndicator_tab_count,COUNT_DEFAULT_TAB);
        if (mTabVisibleCount < 0) {
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }
        COLOR_TEXT_NORMAL = typedArray.getColor(R.styleable.MyTabPageIndicator_COLOR_TEXT_NORMAL,
                COLOR_TEXT_NORMAL);
        COLOR_TEXT_HIGHLIGHTCOLOR = typedArray.getColor(R.styleable.MyTabPageIndicator_COLOR_TEXT_HIGHLIGHTCOLOR,
                COLOR_TEXT_HIGHLIGHTCOLOR);
        typedArray.recycle();

        mScreenWidth = getScreenWidth();
        DIMENSION_TRIANGEL_WIDTH = (int) (mScreenWidth / 3 * RADIO_TRIANGLE);

        // 初始化画笔
        mPaint = new Paint();
        // 设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
        mPaint.setAntiAlias(true);
        // 设置绘制的颜色
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        // 设置画笔的样式，为FILL，FILL_OR_STROKE，或STROKE
        mPaint.setStyle(Paint.Style.FILL);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setDither(true);
        // 设置绘制路径的效果，如点画线等
        mPaint.setPathEffect(new CornerPathEffect(4));
    }

    /**
     * 设置ViewPager
     */
    public void setViewPager(ViewPager view, int position) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        // 设定响应事件
        mViewPager.setOnPageChangeListener(listener);
        notifyDataSetChanged();
        // 设置当前页
        mViewPager.setCurrentItem(position);
        // 高亮
        highLightTextView(position);
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {

        mPath = new Path();
        /**
         * 设置三角行的高度
         */
        int mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
        // path的moveTo方法将起始轮廓点移至x，y坐标点，默认情况为0,0点
        mPath.moveTo(0, 0);
        // lineTo(float x, float y)方法用于从当前轮廓点绘制一条线段到x，y点
        // 绘制一个实心三角形
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    /**
     * 在setViewPager中调用，
     * 用于创建TabPageIndicator，
     * 内部通过一个for循环不断new View，
     * 然后add到Layout上去，紧接着请求布局。
     */
    public void notifyDataSetChanged() {
        this.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        if (mPagerAdapter == adapter) {
            return;
        }
        mPagerAdapter = adapter;
        final int count = mPagerAdapter.getCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                addTab(i, mPagerAdapter.getPageTitle(i));
            }
            this.requestLayout();
        }
    }

    /**
     * 创建TextView
     *
     * @param position 位置
     * @param title    导航栏标签的文本内容
     */
    private void addTab(final int position, CharSequence title) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                mScreenWidth / mTabVisibleCount,
                LinearLayout.LayoutParams.MATCH_PARENT);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, IndicatorTextSize);
        // 自定义字体
        Typeface typeFace = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/IndicatorFont.ttf");
        tv.setTypeface(typeFace);

        tv.setFocusable(true);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() != position) {
                    mViewPager.setCurrentItem(position);
                }
            }
        });
        tv.setLayoutParams(lp);
        this.addView(tv, position);
    }

    /**
     * 指示器跟随手指滚动，以及容器滚动
     *
     * @param position       位置
     * @param positionOffset 变换度
     */
    private void onScroll(int position, float positionOffset) {
        int tabWidth = getScreenWidth() / mTabVisibleCount;
        // 不断改变偏移量，invalidate
        mTranslationX = getWidth() / mTabVisibleCount * (position + positionOffset);

        // 容器滚动，当移动到最后一个的时候，开始滚动
        // 这里详细讲就是说，当当前的Tab位于第三个的时候（下标是2），从第三个移动到第四个会采取滚动的方式
        if (positionOffset > 0 && position >= (mTabVisibleCount - 2)
                && getChildCount() > mTabVisibleCount && position < (getChildCount() - 2)) {
            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth +
                        (int) (tabWidth * positionOffset), 0);
            } else {
                // 当mTabVisibleCount=1的时候的处理方案
                this.scrollTo(position * tabWidth + (int) (tabWidth * positionOffset), 0);
            }
        }
        // 请求重新绘制View
        // 直接调用invalidate()方法,请求重新draw(),但只会绘制调用者本身。
        invalidate();
    }

    /**
     * 高亮文本
     *
     * @param position 高亮TextView的位置
     */
    protected void highLightTextView(int position) {
        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).getPaint().setFakeBoldText(true);
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
        }

    }

    /**
     * 重置文本颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).getPaint().setFakeBoldText(false);
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 设置可见的tab的数量
     *
     * @param count 可见的Tab的数量
     */
    public void setVisibleTabCount(int count) {
        this.mTabVisibleCount = count;
    }

    /**
     * 初始化三角性的宽度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 三角形的宽度
        mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE);
        // 去最小值
        mTriangleWidth = Math.min(DIMENSION_TRIANGEL_WIDTH, mTriangleWidth);
        // 初始化三角形
        initTriangle();

        // 初始时的偏移量
        mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth / 2;
    }

    /**
     * 绘制指示器
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        // 画笔移动到正确的位置
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 获取屏幕的宽度
     *
     * @return 屏幕的宽度
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().
                getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 对外的ViewPager的回调接口
     */
    public interface PageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

}