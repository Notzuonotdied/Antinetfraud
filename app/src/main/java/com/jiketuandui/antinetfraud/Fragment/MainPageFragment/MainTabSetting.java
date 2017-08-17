package com.jiketuandui.antinetfraud.Fragment.MainPageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Activity.SettingActivity.AboutActivity;
import com.jiketuandui.antinetfraud.Activity.SettingActivity.CollectionDetailActivity;
import com.jiketuandui.antinetfraud.Activity.SettingActivity.FeedbackActivity;
import com.jiketuandui.antinetfraud.Activity.SettingActivity.HistoryDetailActivity;
import com.jiketuandui.antinetfraud.Activity.SettingActivity.ShareActivity;
import com.jiketuandui.antinetfraud.Activity.UserActivity.LoginActivity;
import com.jiketuandui.antinetfraud.HTTP.getAppUpdate;
import com.jiketuandui.antinetfraud.HTTP.getImage;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.CacheCleanManage;
import com.jiketuandui.antinetfraud.Util.MyApplication;

import java.io.File;

/**
 * 2016年9月10日 23:38:32
 * 设置页面的编写
 */
public class MainTabSetting extends Fragment {
    private LinearLayout setting_pieces_delete;
    private TextView setting_cache_size;
    private SimpleDraweeView lcholderimage;
    private LinearLayout settingpiecesaccount;
    private TextView settingpiecescollection;
    private TextView settingpieceshistory;
    private TextView settingpiecessetting;
    private TextView settingpiecesidea;
    private TextView settingpiecesupdate;
    private TextView settingpiecesabout;
    private TextView status;
    //    private TextView settingcachesize;
//    private LinearLayout settingpiecesdelete;
    private TextView settingshare;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.setting_pieces_idea:// 意见反馈
                    gotoActivity(FeedbackActivity.class);
                    break;
                case R.id.setting_pieces_about:// 关于
                    gotoActivity(AboutActivity.class);
                    break;
                case R.id.setting_pieces_delete:// 清除缓存
//                    boolean isSuccess = CacheCleanManage.CleanImageCache();
//                    Toast.makeText(getActivity(), isSuccess ? "缓存清除成功" :
//                            "缓存清除失败", Toast.LENGTH_SHORT).show();
//                    setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
                    break;
                case R.id.setting_share:// 分享经历
                    gotoActivity(ShareActivity.class);
                    break;
                case R.id.setting_pieces_update:// app更新
                    getAppUpdate update = new getAppUpdate(getContext(), true);
                    update.startUpdate();
                    break;
                case R.id.setting_pieces_account: // 账号信息
                    LogIn();
                    break;
                case R.id.setting_pieces_history:
                    startActivity(new Intent(getActivity(), HistoryDetailActivity.class));
                    break;
                case R.id.setting_pieces_collection:
                    startActivity(new Intent(getActivity(), CollectionDetailActivity.class));
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_my, container, false);
        this.settingshare = (TextView) view.findViewById(R.id.setting_share);
        this.settingpiecesabout = (TextView) view.findViewById(R.id.setting_pieces_about);
        this.settingpiecesupdate = (TextView) view.findViewById(R.id.setting_pieces_update);
        this.settingpiecesidea = (TextView) view.findViewById(R.id.setting_pieces_idea);
        this.settingpiecessetting = (TextView) view.findViewById(R.id.setting_pieces_setting);
        this.settingpieceshistory = (TextView) view.findViewById(R.id.setting_pieces_history);
        this.settingpiecescollection = (TextView) view.findViewById(R.id.setting_pieces_collection);
        this.settingpiecesaccount = (LinearLayout) view.findViewById(R.id.setting_pieces_account);
        this.lcholderimage = (SimpleDraweeView) view.findViewById(R.id.lc_holder_image);
        this.setting_pieces_delete = (LinearLayout) view.findViewById(R.id.setting_pieces_delete);
        this.setting_cache_size = (TextView) view.findViewById(R.id.setting_cache_size);
        this.status = (TextView) view.findViewById(R.id.status);

        initStatus();
        initLintener();
        return view;
    }

    private void LogIn() {
        if (!MyApplication.getInstance().getLogin()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
        }
    }

    private void initStatus() {
        if (MyApplication.getInstance().getLogin()) {
            this.status.setText(getString(R.string.logged));
        } else {
            this.status.setText(getString(R.string.not_logged_in));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initStatus();
    }

    /**
     * 初始化View的行为事件
     */
    private void initLintener() {
        setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
        setting_pieces_delete.setOnClickListener(listener);
        settingpiecesabout.setOnClickListener(listener);
        settingpiecesidea.setOnClickListener(listener);
        settingshare.setOnClickListener(listener);
        settingpiecesupdate.setOnClickListener(listener);
        settingpiecesaccount.setOnClickListener(listener);
        settingpieceshistory.setOnClickListener(listener);
        settingpiecescollection.setOnClickListener(listener);
    }

    /**
     * 跳转Activity
     */
    private void gotoActivity(Class<?> ActivityClass) {
        Intent intent = new Intent(getActivity(), ActivityClass);
        startActivity(intent);
    }

    @Override // 重置缓存数据
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        setting_cache_size.setText(!hidden ? CacheCleanManage.getCacheSize(new File(getImage.photoPath))
//                : "0.0B");
    }

}
