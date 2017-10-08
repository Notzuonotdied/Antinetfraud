package com.jiketuandui.antinetfraud.Fragment.MainPageFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 2016年9月10日 23:38:32
 * 设置页面的编写
 */
public class MainTabSetting extends Fragment {
    @BindView(R.id.setting_pieces_delete)
    LinearLayoutCompat setting_pieces_delete;
    @BindView(R.id.setting_cache_size)
    AppCompatTextView setting_cache_size;
    @BindView(R.id.lc_holder_image)
    SimpleDraweeView accountPic;
    @BindView(R.id.setting_pieces_account)
    LinearLayoutCompat setAccount;
    @BindView(R.id.setting_pieces_collection)
    AppCompatTextView setCollection;
    @BindView(R.id.setting_pieces_history)
    AppCompatTextView setHistory;
    @BindView(R.id.setting_pieces_idea)
    AppCompatTextView setIdea;
    @BindView(R.id.setting_pieces_update)
    AppCompatTextView setUpdate;
    @BindView(R.id.setting_pieces_about)
    AppCompatTextView setAbout;
    @BindView(R.id.status)
    AppCompatTextView status;
    @BindView(R.id.setting_share)
    AppCompatTextView setShare;

    private View.OnClickListener listener = view -> {
        switch (view.getId()) {
            case R.id.setting_pieces_idea:// 意见反馈
                gotoActivity(FeedbackActivity.class);
                break;
            case R.id.setting_pieces_about:// 关于
                gotoActivity(AboutActivity.class);
                break;
            case R.id.setting_pieces_delete:// 清除缓存
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                imagePipeline.clearCaches();
                setting_cache_size.setText(getFormatSize());
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

    /**
     * 格式化缓存的单位
     */
    private static String getFormatSize() {
        double size = Fresco.getImagePipelineFactory().getBitmapCountingMemoryCache().getSizeInBytes();
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraByte = gigaByte / 1024;
        if (teraByte < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }

        BigDecimal result4 = new BigDecimal(teraByte);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_my, container, false);
        ButterKnife.bind(this, view);
        initStatus();
        initListener();
        CFontTitleTextView title = (CFontTitleTextView) view.findViewById(R.id.main_header_tv);
        title.setOnClickListener(v -> YoYo.with(Techniques.Swing)
                .duration(333)
                .repeat(6)
                .playOn(v.findViewById(R.id.main_header_tv)));
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
        setting_cache_size.setText(getFormatSize());
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
        setting_cache_size.setText(!hidden ? getFormatSize() : "0.0B");

    }
}
