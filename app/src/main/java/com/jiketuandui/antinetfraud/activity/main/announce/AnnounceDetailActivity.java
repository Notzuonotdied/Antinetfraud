package com.jiketuandui.antinetfraud.activity.main.announce;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.StatusBarUtil;
import com.jiketuandui.antinetfraud.entity.domain.AnnounceDetail;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.AnnounceService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangyu
 * @date 2018年02月02日10:17:18
 * @describe 公告详情页面
 */
public class AnnounceDetailActivity extends AppCompatActivity {

    @BindView(R.id.announce_title)
    AppCompatTextView announceTitle;
    @BindView(R.id.announce_time)
    AppCompatTextView announceTime;
    @BindView(R.id.announce_content)
    AppCompatTextView announceContent;
    private AnnounceService announceService = RetrofitServiceFactory.ANNOUNCE_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_detail);
        ButterKnife.bind(this);
        StatusBarUtil.StatusBarLightMode(this);

        loadingArticle();
    }

    /**
     * 获取公告内容
     */
    private void loadingArticle() {
        // 根据ID获取公告的内容
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        announceService.getAnnounceDetail(bundle.getInt(Constants.ANNOUNCE_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<AnnounceDetail>(this) {
                    @Override
                    protected void onHandleSuccess(AnnounceDetail announceDetail) {
                        announceTitle.setText(Html.fromHtml(announceDetail.getTitle()));
                        announceTime.setText(Html.fromHtml(announceDetail.getCreated_at()));
                        announceContent.setText(Html.fromHtml(announceDetail.getContent()));
                    }
                });
    }
}
