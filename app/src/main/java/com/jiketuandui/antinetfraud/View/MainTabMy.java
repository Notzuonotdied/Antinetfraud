package com.jiketuandui.antinetfraud.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiketuandui.antinetfraud.R;

/**
 * 2016年9月10日 23:38:32
 * 设置页面的编写
 * */
public class MainTabMy extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.main_tab_my, container, false);
    }
}
