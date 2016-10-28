package com.jiketuandui.antinetfraud.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.HTTP.getImage;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.CacheCleanManage;

import java.io.File;

/**
 * 2016年9月10日 23:38:32
 * 设置页面的编写
 */
public class MainTabMy extends Fragment {
    private LinearLayout setting_pieces_delete;
    private TextView setting_cache_size;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_my, container, false);
        setting_pieces_delete = (LinearLayout) view.findViewById(R.id.setting_pieces_delete);
        setting_cache_size = (TextView) view.findViewById(R.id.setting_cache_size);

        initViewAction();
        return view;
    }

    /**
     * 初始化View的行为事件
     */
    private void initViewAction() {
        setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
        setting_pieces_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSuccess = CacheCleanManage.CleanImageCache();
                Toast.makeText(getActivity(), isSuccess ? "缓存清除成功" :
                        "缓存清除失败", Toast.LENGTH_SHORT).show();
                setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setting_cache_size.setText(!hidden ? CacheCleanManage.getCacheSize(new File(getImage.photoPath))
                : "0.0B");
    }
}
