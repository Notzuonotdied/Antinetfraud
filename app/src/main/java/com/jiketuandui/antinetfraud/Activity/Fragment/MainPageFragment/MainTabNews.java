package com.jiketuandui.antinetfraud.Activity.Fragment.MainPageFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.entity.dto.Result;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Notzuonotdied
 * @date 2016/8/1
 * 这个是放置主页新闻ViewPage的内容的
 */
public class MainTabNews extends Fragment implements NetBroadcastReceiver.netEventHandler {

    private int readPage;
    /**
     * 是否是热门案例，暂时通过是否有bundle来判断是热门还是正常。
     */
    private boolean isHot;
    private List<ArticleList.DataBean> mListContents = new ArrayList<>();
    private MaterialRefreshLayout materialRefreshLayout;
    private RecyclerView mRecyclerView;
    private ListContentAdapter mListContentAdapter;
    /**
     * 在上拉刷新的时候，判断，是否处于上拉刷新，如果是的话，就禁止在一次刷新，保障在数据加载完成之前
     * 避免重复和多次加载
     */
    private boolean isFirstRefresh = true;
    private boolean isNeed2Refresh = false;
    private boolean isOpenTop = true;
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData(true);

        View view = initView(inflater, R.layout.main_tab_news, false);
        setViewAttr();

        return view;
    }

    protected View initView(@NonNull LayoutInflater inflater,
                            int layoutId,
                            boolean noIndicator) {
        View view = inflater.inflate(layoutId, null);
        this.materialRefreshLayout = view.findViewById(R.id.refresh);
        this.mRecyclerView = view.findViewById(R.id.recyclerView);
        if (noIndicator) {
            CFontTitleTextView title = view.findViewById(R.id.main_header_tv);
            title.setOnClickListener(v -> YoYo.with(Techniques.Swing)
                    .duration(333)
                    .repeat(6)
                    .playOn(v.findViewById(R.id.main_header_tv)));
        }
        setViewAttr();
        return view;
    }

    protected void initData(boolean hasBundle) {
        if (hasBundle) {
            // 获取当前Item的下标
            Bundle bundle = getArguments();
            assert bundle != null;
            int position = bundle.getInt(MyApplication.getInstance().getMAINPAGEPOSITON());
            this.isOpenTop = position == 0;
            isHot = false;
        } else {
            this.isOpenTop = true;
            this.isHot = true;
        }
        readPage = 1;

        // 注册
        NetBroadcastReceiver.mListeners.add(this);
    }

    @Override
    public void onResume() {
        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            if (NetWorkUtils.isConnectNET(getContext())) {
                materialRefreshLayout.autoRefresh();
                isFirstRefresh = false;
            } else {
                isFirstRefresh = true;
            }
        }
        super.onResume();
    }

    /**
     * 初始化控件
     */
    private void setViewAttr() {
        /* Adapter：使用RecyclerView之前，你需要一个继承自RecyclerView.Adapter的适配器，
         * 作用是将数据与每一个item的界面进行绑定。
         * */
        mListContentAdapter = new ListContentAdapter(getActivity(), mListContents,
                isOpenTop, 1);
        /* LayoutManager：用来确定每一个item如何进行排列摆放，何时展示和隐藏。
         * 回收或重用一个View的时候，LayoutManager会向适配器请求新的数据来替换旧的数据，
         * 这种机制避免了创建过多的View和频繁的调用findViewById方法（与ListView原理类似）。
         * */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mListContentAdapter);

        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            if (NetWorkUtils.isConnectNET(getContext())) {
                materialRefreshLayout.autoRefresh();
                isFirstRefresh = false;
            } else {
                isFirstRefresh = true;
            }
        }

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(getContext())) {
                    if (isHot) {
                        MainTabNews.this.onRefresh(articleService.getHotArticleList(readPage));
                    } else {
                        MainTabNews.this.onRefresh(articleService.getArticleList(readPage));
                    }
                } else {
                    materialRefreshLayout.finishRefresh();
                    isFirstRefresh = true;
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(getContext())) {
                    if (isHot) {
                        MainTabNews.this.onLoadMore(articleService.getHotArticleList(readPage));
                    } else {
                        MainTabNews.this.onLoadMore(articleService.getArticleList(readPage));
                    }
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    @Override
    public void onNetChange() {
        if (MyApplication.getInstance().getNetWorkState() != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mListContents != null && mListContents.size() == 0) {
            materialRefreshLayout.autoRefresh();
        }
    }

    private void onRefresh(Observable<Result<ArticleList>> articleList) {
        readPage = 1;
        articleList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(articleListResult -> {
                    // 将Image URL补全
                    for (ArticleList.DataBean dataBean : articleListResult.getData().getData()) {
                        dataBean.setImage(RetrofitServiceFactory.IMAGE_URL + dataBean.getImage());
                    }
                    return articleListResult;
                })
                .subscribe(new BaseObserver<ArticleList>(getContext()) {
                    @Override
                    protected void onHandleSuccess(ArticleList articleList) {
                        // 填充容器并刷新页面的列表项数据
                        isNeed2Refresh = true;
                        mListContentAdapter.setData(articleList.getData());
                        mListContentAdapter.notifyDataSetChanged();
                        materialRefreshLayout.finishRefresh();
                    }
                });
    }

    private void onLoadMore(Observable<Result<ArticleList>> articleList) {
        articleList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(articleListResult -> {
                    // 将Image URL补全
                    for (ArticleList.DataBean dataBean : articleListResult.getData().getData()) {
                        dataBean.setImage(RetrofitServiceFactory.IMAGE_URL + dataBean.getImage());
                    }
                    return articleListResult;
                })
                .subscribe(new BaseObserver<ArticleList>(getContext()) {
                    @Override
                    protected void onHandleSuccess(ArticleList articleList) {
                        ++readPage;
                        if (articleList.getNext_page_url() == null) {
                            if (!isNeed2Refresh) {
                                Toast.makeText(getContext(), "已到底部~", Toast.LENGTH_SHORT).show();
                                materialRefreshLayout.finishRefreshLoadMore();
                                return;
                            }
                            isNeed2Refresh = true;
                            materialRefreshLayout.finishRefreshLoadMore();
                            return;
                        }
                        mListContentAdapter.addData(articleList.getData());
                        mListContentAdapter.notifyDataSetChanged();
                        materialRefreshLayout.finishRefreshLoadMore();
                    }
                });
    }
}
