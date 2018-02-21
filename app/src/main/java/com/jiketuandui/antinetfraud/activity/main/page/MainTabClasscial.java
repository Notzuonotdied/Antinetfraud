package com.jiketuandui.antinetfraud.activity.main.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiketuandui.antinetfraud.activity.article.ArticleContentActivity;
import com.jiketuandui.antinetfraud.Adapter.VideoListAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;
import com.jiketuandui.antinetfraud.View.MyListView;
import com.jiketuandui.antinetfraud.View.banner.BannerBaseView;
import com.jiketuandui.antinetfraud.View.banner.MainBannerView;
import com.jiketuandui.antinetfraud.View.banner.bean.BaseBannerBean;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 典型案例页面
 *
 * @author wangyu
 */
public class MainTabClasscial extends Fragment implements NetBroadcastReceiver.NetEventHandler {

    private RelativeLayout bannerContent;
    private AppCompatTextView tv01;
    private AppCompatTextView tv02;
    private CFontTitleTextView title;
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;

    /**
     * 当前页面的各个Item的数据存放容器
     */
    private List<ArticleList.DataBean> bannerListContents = new ArrayList<>();
    private List<String> bannerTitle = new ArrayList<>();

    private View.OnClickListener tvListener = view -> {
        switch (view.getId()) {
            case R.id.tv_01:
                ToastUtils.showShort("正在抓紧开发中~");
                break;
            case R.id.tv_02:
                ToastUtils.showShort("正在抓紧开发中~");
                break;
            case R.id.main_header_tv:
                YoYo.with(Techniques.Swing)
                        .duration(333)
                        .repeat(6)
                        .playOn(view.findViewById(R.id.main_header_tv));
                break;
            default:
                break;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_classcial, container, false);
        initAllView(view);
        initView();
        // 注册
        NetBroadcastReceiver.mListeners.add(this);
        return view;
    }

    private void initAllView(View view) {
        bannerContent = view.findViewById(R.id.search_banner_cont);

        MyListView listView = view.findViewById(R.id.listview);
        this.tv01 = view.findViewById(R.id.tv_01);
        this.tv02 = view.findViewById(R.id.tv_02);
        this.title = view.findViewById(R.id.main_header_tv);

        VideoListAdapter adapterVideoList = new VideoListAdapter(getActivity());
        listView.setAdapter(adapterVideoList);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        initBanner();
        initListener();
    }

    private void initBanner() {
        articleService.getArticleList(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(articleListResult -> {
                    // 将Image URL补全
                    for (ArticleList.DataBean dataBean : articleListResult.getData().getData()) {
                        dataBean.setImage(RetrofitServiceFactory.IMAGE_URL + dataBean.getImage());
                    }
                    return articleListResult;
                })
                .subscribe(new BaseObserver<ArticleList>(getActivity().getApplicationContext()) {
                    @Override
                    protected void onHandleSuccess(ArticleList articleList) {
                        bannerListContents = articleList.getData();
                        BannerBaseView banner = new MainBannerView(getActivity());
                        List<BaseBannerBean> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(new BaseBannerBean(bannerListContents.get(i).getImage()));
                            bannerTitle.add(bannerListContents.get(i).getTitle());
                        }
                        banner.setBannerViewOnClickListener(position -> {
                            if (NetWorkUtils.isConnectNET(getActivity())) {
                                Intent intent = new Intent(getActivity(), ArticleContentActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putInt(Constants.CONTENT_ID, bannerListContents.get(position).getId());
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }
                        });
                        bannerContent.addView(banner);
                        banner.setBannerTitle(bannerTitle);
                        banner.update(list);
                    }
                });
    }

    private void initListener() {
        tv01.setOnClickListener(tvListener);
        tv02.setOnClickListener(tvListener);
        title.setOnClickListener(tvListener);
    }

    @Override
    public void onPause() {
        JCVideoPlayer.releaseAllVideos();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetChange() {
        if (MyApplication.NetWorkState != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                bannerListContents != null && bannerListContents.size() == 0) {
            initBanner();
        }
    }
}
