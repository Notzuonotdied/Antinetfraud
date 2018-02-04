package com.jiketuandui.antinetfraud.activity.main.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;
import com.jiketuandui.antinetfraud.activity.setting.AboutActivity;
import com.jiketuandui.antinetfraud.activity.setting.CollectionDetailActivity;
import com.jiketuandui.antinetfraud.activity.setting.FeedbackActivity;
import com.jiketuandui.antinetfraud.activity.setting.HistoryDetailActivity;
import com.jiketuandui.antinetfraud.activity.setting.ShareActivity;
import com.jiketuandui.antinetfraud.activity.setting.update.getAppUpdate;
import com.jiketuandui.antinetfraud.activity.setting.user.LoginActivity;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置页面的编写
 *
 * @author wangyu
 * @date 2016年9月10日 23:38:32
 */
public class MainTabSetting extends Fragment {
    @BindView(R.id.setting_pieces_delete)
    LinearLayoutCompat settingPiecesDelete;
    @BindView(R.id.setting_cache_size)
    AppCompatTextView settingCacheSize;
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
            // 意见反馈
            case R.id.setting_pieces_idea:
                gotoActivity(FeedbackActivity.class);
                break;
            // 关于
            case R.id.setting_pieces_about:
                gotoActivity(AboutActivity.class);
                break;
            // 清除缓存
            case R.id.setting_pieces_delete:
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                imagePipeline.clearCaches();
                settingCacheSize.setText(getFormatSize());
                break;
            // 分享经历
            case R.id.setting_share:
                gotoActivity(ShareActivity.class);
                break;
            // app更新
            case R.id.setting_pieces_update:
                getAppUpdate update = new getAppUpdate(true);
                update.startUpdate();
                break;
            // 账号信息
            case R.id.setting_pieces_account:
                logIn();
                break;
            case R.id.setting_pieces_history:
                if (MyApplication.getInstance().getUser() == null) {
                    ToastUtils.showShort("请登录～");
                } else {
                    startActivity(new Intent(getActivity(), HistoryDetailActivity.class));
                }
                break;
            case R.id.setting_pieces_collection:
                if (MyApplication.getInstance().getUser() == null) {
                    ToastUtils.showShort("请登录～");
                } else {
                    startActivity(new Intent(getActivity(), CollectionDetailActivity.class));
                }
                break;
            default:
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_my, container, false);
        ButterKnife.bind(this, view);
        initStatus();
        initListener();
        CFontTitleTextView title = view.findViewById(R.id.main_header_tv);
        title.setOnClickListener(v -> YoYo.with(Techniques.Swing)
                .duration(333)
                .repeat(6)
                .playOn(v.findViewById(R.id.main_header_tv)));
        return view;
    }

    private void logIn() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void initStatus() {
        if (MyApplication.getInstance().isLogin()) {
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
        settingCacheSize.setText(getFormatSize());
        settingPiecesDelete.setOnClickListener(listener);
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
    private void gotoActivity(Class<?> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }

    /**
     * 重置缓存数据
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        settingCacheSize.setText(!hidden ? getFormatSize() : "0.0B");
    }
}
