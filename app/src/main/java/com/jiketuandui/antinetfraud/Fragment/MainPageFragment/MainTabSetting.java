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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 2016年9月10日 23:38:32
 * 设置页面的编写
 */
public class MainTabSetting extends Fragment {
    @BindView(R.id.setting_pieces_delete)
    LinearLayout setting_pieces_delete;
    @BindView(R.id.setting_cache_size)
    TextView setting_cache_size;
    @BindView(R.id.lc_holder_image)
    SimpleDraweeView acountPicture;
    @BindView(R.id.setting_pieces_account)
    LinearLayout setAccount;
    @BindView(R.id.setting_pieces_collection)
    TextView setCollection;
    @BindView(R.id.setting_pieces_history)
    TextView setHistory;
    @BindView(R.id.setting_pieces_idea)
    TextView setIdea;
    @BindView(R.id.setting_pieces_update)
    TextView setUpdate;
    @BindView(R.id.setting_pieces_about)
    TextView setAbout;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.setting_share)
    TextView setShare;

    private View.OnClickListener listener = view -> {
        switch (view.getId()) {
            case R.id.setting_pieces_idea:// 意见反馈
                gotoActivity(FeedbackActivity.class);
                break;
            case R.id.setting_pieces_about:// 关于
                gotoActivity(AboutActivity.class);
                break;
            case R.id.setting_pieces_delete:// 清除缓存
//                boolean isSuccess = CacheCleanManage.CleanImageCache();
//                Toast.makeText(getActivity(), isSuccess ? "缓存清除成功" :
//                        "缓存清除失败", Toast.LENGTH_SHORT).show();
                // setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                imagePipeline.clearCaches();
                setting_cache_size.setText(
                        String.valueOf(
                                Fresco.getImagePipelineFactory().getBitmapCountingMemoryCache().getSizeInBytes()
                        ));
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
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_my, container, false);
        ButterKnife.bind(this, view);
        initStatus();
        initListener();
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
    private void initListener() {
        setting_cache_size.setText(CacheCleanManage.getCacheSize(new File(getImage.photoPath)));
        setting_pieces_delete.setOnClickListener(listener);
        setAbout.setOnClickListener(listener);
        setIdea.setOnClickListener(listener);
        setShare.setOnClickListener(listener);
        setUpdate.setOnClickListener(listener);
        setAccount.setOnClickListener(listener);
        setHistory.setOnClickListener(listener);
        setCollection.setOnClickListener(listener);
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
//        setting_cache_size.setText(
//                !hidden ? CacheCleanManage.getCacheSize(new File(getImage.photoPath))
//                        : "0.0B");
        setting_cache_size.setText(!hidden ? String.valueOf(
                Fresco.getImagePipelineFactory().getBitmapCountingMemoryCache().getSizeInBytes())
                : "0.0B");

    }

}
