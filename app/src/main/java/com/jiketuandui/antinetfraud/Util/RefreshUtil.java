package com.jiketuandui.antinetfraud.Util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.View.ToolBarLayout;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.entity.dto.Result;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 内容刷新工具类
 *
 * @author wangyu
 * @date 18-2-2
 */

public class RefreshUtil {
    public ToolBarLayout tagsTitle;
    public RecyclerView tagsRecyclerView;
    public MaterialRefreshLayout materialRefreshLayout;
    private int readPage;
    private int tagId;
    private boolean isFirstRefresh = true;
    /**
     * 在上拉刷新的时候，判断，是否处于上拉刷新，如果是的话，就禁止在一次刷新，保障在数据加载完成之前
     * 避免重复和多次加载
     */
    private boolean isNeed2Refresh = false;
    /**
     * 是否开启标签栏，默认为不开
     */
    private boolean isOpenTop = false;
    private ListContentAdapter mListContentAdapter;
    private List<ArticleList.DataBean> mListContents = new ArrayList<>();
    private Context context;
    private RefreshInterface refreshInterface;
    private Response response;

    public RefreshUtil(Context context) {
        this.context = context;
        this.readPage = 1;
        this.tagId = 0;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setOpenTop(boolean openTop) {
        isOpenTop = openTop;
    }

    public int getReadPage() {
        return readPage;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public void setRefreshInterface(RefreshInterface refreshInterface) {
        this.refreshInterface = refreshInterface;
    }

    private void initListener() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(context)) {
                    readPage = 1;
                    refreshInterface.refresh();
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(context)) {
                    refreshInterface.loadMore();
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    public void initViewAttr() {
        this.mListContentAdapter = new ListContentAdapter(context, mListContents, isOpenTop);
        this.tagsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        this.tagsRecyclerView.setAdapter(mListContentAdapter);
        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            materialRefreshLayout.autoRefresh();
            isFirstRefresh = false;
        }
        if (tagsTitle != null && tagId != 0) {
            this.tagsTitle.setText(Constants.TAB_BIG_TITLE[tagId]);
        }
        initListener();
    }

    public void onRefresh(Observable<Result<ArticleList>> articleList) {
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
                .subscribe(new BaseObserver<ArticleList>(context.getApplicationContext()) {
                    @Override
                    protected void onHandleSuccess(ArticleList articleList) {
                        // 填充容器并刷新页面的列表项数据
                        isNeed2Refresh = true;
                        mListContentAdapter.setData(articleList.getData());
                        mListContentAdapter.notifyDataSetChanged();
                        materialRefreshLayout.finishRefresh();
                    }

                    @Override
                    protected void onHandleFailure(String message) {
                        super.onHandleFailure(message);
                        Toast.makeText(context.getApplicationContext(), message,
                                Toast.LENGTH_SHORT).show();
                        materialRefreshLayout.finishRefresh();
                    }
                });
    }

    public void onLoadMore(Observable<Result<ArticleList>> articleList) {
        articleList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(articleListResult -> {
                    // 将Image URL补全
                    for (ArticleList.DataBean dataBean : articleListResult.getData().getData()) {
                        dataBean.setImage(RetrofitServiceFactory.IMAGE_URL + dataBean.getImage());
                    }
                    return articleListResult;
                })
                .subscribe(new BaseObserver<ArticleList>(context.getApplicationContext()) {
                    @Override
                    protected void onHandleSuccess(ArticleList articleList) {
                        ++readPage;
                        if (articleList.getNext_page_url() == null) {
                            if (!isNeed2Refresh) {
                                show("已到底部~");
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

                        if (response != null) {
                            response.onSuccess();
                        }
                    }

                    @Override
                    protected void onHandleFailure(String message) {
                        super.onHandleFailure(message);
                        materialRefreshLayout.finishRefreshLoadMore();
                        if (response != null) {
                            response.onFailure();
                        }
                    }
                });
    }

    public void show(String message) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(),
                    message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onResume() {
        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            if (NetWorkUtils.isConnectNET(context)) {
                materialRefreshLayout.autoRefresh();
                isFirstRefresh = false;
            } else {
                isFirstRefresh = true;
            }
        }
    }

    /**
     * 自动刷新
     */
    public void onNetChange() {
        if (MyApplication.NetWorkState != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mListContents != null && mListContents.size() == 0) {
            materialRefreshLayout.autoRefresh();
        }
    }

    /**
     * 自动刷新
     */
    public void onNetChangeOnSearch(String inputString) {
        if (MyApplication.NetWorkState != NetWorkUtils.NET_TYPE_NO_NETWORK
                && mListContents != null
                && mListContents.size() == 0
                && inputString != null) {
            materialRefreshLayout.autoRefresh();
        }
    }

    public void clearData() {
        mListContents.clear();
        mListContentAdapter.notifyDataSetChanged();
        readPage = 1;
    }

    public void setGone() {
        materialRefreshLayout.setVisibility(View.GONE);
    }

    public void setVisible() {
        materialRefreshLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 上拉刷新和下拉加载更多对外接口
     */
    public interface RefreshInterface {
        /**
         * 刷新
         */
        void refresh();

        /**
         * 加载更多
         */
        void loadMore();
    }

    /**
     * 网络请求响应
     */
    public interface Response {
        /**
         * 成功回调
         */
        void onSuccess();

        /**
         * 失败回调
         */
        void onFailure();
    }
}
