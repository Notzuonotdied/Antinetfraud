package com.jiketuandui.antinetfraud.activity.main.announce;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.AnnounceAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.Util.StatusBarUtil;
import com.jiketuandui.antinetfraud.entity.domain.AnnounceList;
import com.jiketuandui.antinetfraud.entity.dto.Result;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.AnnounceService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 公告列表
 *
 * @author wangyu
 */
public class AnnounceActivity extends AppCompatActivity {
    @BindView(R.id.announce_refresh)
    MaterialRefreshLayout materialRefreshLayout;
    /**
     * 页数
     */
    private int readPage;
    /**
     * 在上拉刷新的时候，判断，是否处于上拉刷新，如果是的话，就禁止在一次刷新，保障在数据加载完成之前
     * 避免重复和多次加载
     */
    private boolean isFirstRefresh = true;
    private boolean isNeed2Refresh = false;
    private AnnounceAdapter mListContentAdapter;
    private List<AnnounceList.DataBean> mListContents = new ArrayList<>();
    private AnnounceService announceService = RetrofitServiceFactory.ANNOUNCE_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);
        ButterKnife.bind(this);
        StatusBarUtil.StatusBarLightMode(this);

        initView();
        setViewAttr();
    }

    @Override
    public void onResume() {
        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                materialRefreshLayout.autoRefresh();
                isFirstRefresh = false;
            } else {
                isFirstRefresh = true;
            }
        }
        super.onResume();
    }

    /**
     * 初始化响应事件
     */
    private void setViewAttr() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                    AnnounceActivity.this.onRefresh(announceService.getAnnounceList(readPage));
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                    AnnounceActivity.this.onLoadMore(announceService.getAnnounceList(readPage));
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 初始化View
     */
    private void initView() {
        mListContentAdapter = new AnnounceAdapter(AnnounceActivity.this, mListContents);
        RecyclerView mRecyclerView = findViewById(R.id.announce_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AnnounceActivity.this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mListContentAdapter);

        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                materialRefreshLayout.autoRefresh();
                isFirstRefresh = false;
            } else {
                isFirstRefresh = true;
            }
        }
    }

    private void onRefresh(Observable<Result<AnnounceList>> announceList) {
        readPage = 1;
        announceList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<AnnounceList>(this) {
                    @Override
                    protected void onHandleSuccess(AnnounceList announceList) {
                        // 填充容器并刷新页面的列表项数据
                        isNeed2Refresh = true;
                        mListContentAdapter.setData(announceList.getData());
                        mListContentAdapter.notifyDataSetChanged();
                        materialRefreshLayout.finishRefresh();
                    }
                });
    }

    private void onLoadMore(Observable<Result<AnnounceList>> announceList) {
        announceList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<AnnounceList>(this) {
                    @Override
                    protected void onHandleSuccess(AnnounceList announceList) {
                        ++readPage;
                        if (announceList.getNext_page_url() == null) {
                            if (!isNeed2Refresh) {
                                ToastUtils.showShort("已到底部~");
                                materialRefreshLayout.finishRefreshLoadMore();
                                return;
                            }
                            isNeed2Refresh = true;
                            materialRefreshLayout.finishRefreshLoadMore();
                            return;
                        }
                        mListContentAdapter.addData(announceList.getData());
                        mListContentAdapter.notifyDataSetChanged();
                        materialRefreshLayout.finishRefreshLoadMore();
                    }
                });
    }
}
