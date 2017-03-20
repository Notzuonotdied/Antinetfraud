package com.jiketuandui.antinetfraud.Activity.UtilActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

public class ToTagsListActivity extends Activity {
    private int readPage;
    private int tagId;
    private int category;
    private boolean isFirstRefresh = true;
    private boolean isNeedtoRefresh = false;
    private ListContentAdapter mListContentAdapter;
    private List<ListContent> mListContents = new ArrayList<>();
    private android.widget.FrameLayout tagsback;
    private android.widget.TextView tagstitle;
    private android.support.v7.widget.RecyclerView tagsrecyclerView;
    private com.cjj.MaterialRefreshLayout tagsrefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_tags_list);
        readPage = 1;
        tagId = getIntent().getExtras().getInt(MyApplication.TAGSID);
        category = getIntent().getExtras().getInt(MyApplication.CATEGORY);
        initView();
        initLintener();
    }

    private void initLintener() {
        this.tagsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tagsrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(ToTagsListActivity.this)) {
                    new RefreshDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(ToTagsListActivity.this)) {
                    new LoadMoreDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void initView() {
        this.mListContentAdapter = new ListContentAdapter(ToTagsListActivity.this, mListContents,false);
        this.tagsrefresh = (MaterialRefreshLayout) findViewById(R.id.tags_refresh);
        this.tagsrecyclerView = (RecyclerView) findViewById(R.id.tags_recyclerView);
        this.tagstitle = (TextView) findViewById(R.id.tags_title);
        this.tagsback = (FrameLayout) findViewById(R.id.tags_back);
        this.tagsrecyclerView.setLayoutManager(new LinearLayoutManager(ToTagsListActivity.this,
                LinearLayoutManager.VERTICAL, false));
        this.tagsrecyclerView.setAdapter(mListContentAdapter);
        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            tagsrefresh.autoRefresh();
            isFirstRefresh = false;
        }
        this.tagstitle.setText(MyApplication.TabBigTitle[tagId]);
    }

    /**
     * 刷新数据
     */
    class RefreshDataTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            readPage = 1;
            switch (category) {
                case 1:
                    mListContents = ((MyApplication) getApplication()).instanceConnect().setContentURLByTagId(getConnect.UrlContentHead,
                            String.valueOf(readPage), String.valueOf(tagId));
                    break;
                case 2:
                    mListContents = ((MyApplication) getApplication()).instanceConnect().setContentURLByTagId(getConnect.UrlContentHot,
                            String.valueOf(readPage), String.valueOf(tagId));
                    break;
            }

            return mListContents;
        }

        @Override
        protected void onPostExecute(List<ListContent> mListContents) {
            super.onPostExecute(mListContents);
            if (mListContents != null) {
                isNeedtoRefresh = true;
                mListContentAdapter.setData(mListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            tagsrefresh.finishRefresh();
        }
    }

    /**
     * 加载更多数据
     */
    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            List<ListContent> ListContents = null;
            if (mListContentAdapter.getData().size() == 0) {
                return null;
            }
            readPage++;
            isNeedtoRefresh = true;
            switch (category) {
                case 1:
                    ListContents = ((MyApplication) getApplication()).instanceConnect().setContentURLByTagId(getConnect.UrlContentHead,
                            String.valueOf(readPage), String.valueOf(tagId));
                    break;
                case 2:
                    ListContents = ((MyApplication) getApplication()).instanceConnect().setContentURLByTagId(getConnect.UrlContentHot,
                            String.valueOf(readPage), String.valueOf(tagId));
                    break;
            }
            return ListContents;
        }

        @Override
        protected void onPostExecute(List<ListContent> ListContents) {
            super.onPostExecute(ListContents);
            if (ListContents == null) {
                if (!isNeedtoRefresh) {
                    Toast.makeText(ToTagsListActivity.this, "已到底部~", Toast.LENGTH_SHORT).show();
                    tagsrefresh.finishRefreshLoadMore();
                    return;
                }
                isNeedtoRefresh = true;
                tagsrefresh.finishRefreshLoadMore();
                return;
            }
            if (!MyApplication.isContainLists(mListContentAdapter, ListContents)) {
                mListContentAdapter.addData(ListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            tagsrefresh.finishRefreshLoadMore();
        }
    }
}
