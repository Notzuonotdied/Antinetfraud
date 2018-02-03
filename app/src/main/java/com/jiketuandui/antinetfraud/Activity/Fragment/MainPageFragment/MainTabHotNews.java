package com.jiketuandui.antinetfraud.Activity.Fragment.MainPageFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.R;


/**
 * 热门案例
 *
 * @author wangyu
 */
public class MainTabHotNews extends MainTabNews {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, R.layout.main_tab_hot_news, true, false);
    }

}
