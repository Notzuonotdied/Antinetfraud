package com.jiketuandui.antinetfraud.Fragment.MainPageFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.Adapter.MainTabHotAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.View.MyTabPageIndicator;

import java.lang.reflect.Field;


public class MainTabHot extends Fragment {

    public MainTabHot() {
        super();
    }

    /**
     * 因为要在Fragment中嵌套使用ViewPager,所以需要进行初始化一些变量
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /**
         * 使用LayoutInflate将main_tab.xml的加载进来之后,为TabPageIndicator,
         * ViewPager进行初始化
         * */
        View v = inflater.inflate(R.layout.main_tab_hot, container, false);
        initData(v);
        return v;
    }

    /**
     * 初始化一些变量
     */
    private void initData(View v) {
        /**
         * 先实例化ViewPager,然后实例化TabPageIndicator，
         * 并且要设置TabPageIndicator和ViewPager关联，
         * 就是调用TabPageIndicator的setViewPager(ViewPager view)方法，
         * 这样子我们就实现了点击上面的Tab，下面的ViewPager切换，
         * 滑动ViewPager上面的Tab跟着切换，ViewPager的每一个Item我们使用的是Fragment，
         * 使用Fragment可以使我们的布局更加灵活一点
         * */
        MyTabPageIndicator myTabPageIndicator = (MyTabPageIndicator)
                v.findViewById(R.id.mainTab_hot_indicator);
        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.mainTab_hot_viewpager);
        mViewPager.setOffscreenPageLimit(2);
        MainTabHotAdapter mainTab_hotAdapter = new MainTabHotAdapter(getChildFragmentManager());
        /**
         * 绑定
         * */
        mViewPager.setAdapter(mainTab_hotAdapter);
        myTabPageIndicator.setViewPager(mViewPager, 0);
    }


    /**
     * 这段可以解决fragment嵌套fragment会崩溃的问题
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            //参数是固定写法
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
