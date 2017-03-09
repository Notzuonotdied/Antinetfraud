package com.jiketuandui.antinetfraud.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.AnnounceAdapter;
import com.jiketuandui.antinetfraud.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

public class AnnounceActivity extends AppCompatActivity {

    private int readPage;
    private android.widget.FrameLayout back;
    private com.cjj.MaterialRefreshLayout materialRefreshLayout;
    private AnnounceAdapter mListContentAdapter;
    private List<AnnounceContent> mListContents = new ArrayList<>();
    private boolean isFirstRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);
        readPage = 1;
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
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                    new AnnounceActivity.RefreshDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(AnnounceActivity.this)) {
                    new AnnounceActivity.LoadMoreDataTask().execute();
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
        this.materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.announce_refresh);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.announce_recyclerView);
        this.back = (FrameLayout) findViewById(R.id.announce_back);
        mListContentAdapter = new AnnounceAdapter(AnnounceActivity.this, mListContents);
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
            List<AnnounceContent> listContents = ((MyApplication) getApplication())
                    .instanceAnnouncement()
                    .getAnnounceList();
            //readPage++;
            return listContents;
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
            List<AnnounceContent> listContents = ((MyApplication) getApplication())
                    .instanceAnnouncement()
                    .getAnnounceList();
            //readPage++;
            return listContents;
        }

        @Override
        protected void onPostExecute(List<AnnounceContent> ListContents) {
            super.onPostExecute(ListContents);
            if (ListContents == null) {
                materialRefreshLayout.finishRefreshLoadMore();
                return;
            }
            if (!Constant.isContainLists(mListContentAdapter, ListContents)) {
                mListContentAdapter.addData(ListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
    }
}
