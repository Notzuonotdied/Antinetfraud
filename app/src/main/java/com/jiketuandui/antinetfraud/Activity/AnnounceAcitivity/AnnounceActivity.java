package com.jiketuandui.antinetfraud.Activity.AnnounceAcitivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.AnnounceAdapter;
import com.jiketuandui.antinetfraud.HTTP.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.service.AnnounceService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnounceActivity extends AppCompatActivity {
    @BindView(R.id.announce_refresh)
    MaterialRefreshLayout materialRefreshLayout;
    private AnnounceAdapter mListContentAdapter;
    private List<AnnounceContent> mListContents = new ArrayList<>();
    private boolean isFirstRefresh = true;
    private AnnounceService service = RetrofitServiceFactory.ANNOUNCE_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);
        ButterKnife.bind(this);
        initView();
        initListener();
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
    private void initListener() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                    new RefreshDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                    new LoadMoreDataTask().execute();
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

    /**
     * 刷新数据
     */
    private class RefreshDataTask extends AsyncTask<Void, Void, List<AnnounceContent>> {

        @Override
        protected List<AnnounceContent> doInBackground(Void... voids) {
            //readPage++;
            return ((MyApplication) getApplication())
                    .instanceAnnouncement()
                    .getAnnounceList();
        }

        @Override
        protected void onPostExecute(List<AnnounceContent> mListContents) {
            super.onPostExecute(mListContents);
            if (mListContents != null) {
                mListContentAdapter.setData(mListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            materialRefreshLayout.finishRefresh();
        }
    }

    /**
     * 加载更多数据
     */
    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<AnnounceContent>> {

        @Override
        protected List<AnnounceContent> doInBackground(Void... voids) {
            if (mListContentAdapter.getData().size() == 0) {
                return null;
            }
            //readPage++;
            return ((MyApplication) getApplication())
                    .instanceAnnouncement()
                    .getAnnounceList();
        }

        @Override
        protected void onPostExecute(List<AnnounceContent> ListContents) {
            super.onPostExecute(ListContents);
            if (ListContents == null) {
                materialRefreshLayout.finishRefreshLoadMore();
                return;
            }
            if (!MyApplication.getInstance().isContainLists(mListContentAdapter, ListContents)) {
                mListContentAdapter.addData(ListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
    }
}
