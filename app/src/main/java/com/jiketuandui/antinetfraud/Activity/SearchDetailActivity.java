package com.jiketuandui.antinetfraud.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.SQL.RecordSQLiteOpenHelper;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.MySearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchDetailActivity extends AppCompatActivity implements NetBroadcastReceiver.netEventHandler {
    private int readPage;
    private MySearchView mySearchView;
    private android.widget.FrameLayout back;
    private android.widget.TextView searchnull;
    private com.cjj.MaterialRefreshLayout materialRefreshLayout;
    private ListContentAdapter mListContentAdapter;
    private String inputString;
    private RecordSQLiteOpenHelper helper;
    private List<ListContent> mListContents = new ArrayList<>();
    private boolean isNull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search);
        // 注册
        NetBroadcastReceiver.mListeners.add(this);

        helper = new RecordSQLiteOpenHelper(SearchDetailActivity.this);
        inputString = getIntent().getExtras().getString(Constant.SEARCHSTRING);
        readPage = 1;
        initView();
        initListener();
        if (!inputString.equals("")) {
            mySearchView.toSubmit(inputString);
        }
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

        mySearchView.setSearchViewListener(new MySearchView.SearchViewListener() {
            @Override
            public void onSearch(String text) {
                searchfunction(text);
            }

            @Override
            public void onQueryTextSubmit(String text) {
                searchfunction(text);
            }

            @Override
            public void onQueryTextChange(String text) {

            }
        });

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(SearchDetailActivity.this)) {
                    new RefreshDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(SearchDetailActivity.this)) {
                    new LoadMoreDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 搜索执行的函数
     */
    private void searchfunction(String text) {
        inputString = text;
        mListContents.clear();
        mListContentAdapter.notifyDataSetChanged();
        readPage = 1;
        new SearchDataTask().execute(text);

        if (!text.equals("")) {
            if (isNull) {
                searchnull.setVisibility(View.VISIBLE);
                materialRefreshLayout.setVisibility(View.GONE);
            } else {
                materialRefreshLayout.setVisibility(View.VISIBLE);
                searchnull.setVisibility(View.GONE);
            }
        }

        // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
        boolean hasData = helper.hasData(text.trim());
        if (!hasData) {
            helper.insertData(text.trim());
        }

        closeIME();
    }

    /**
     * 初始化View
     */
    private void initView() {
        this.mySearchView = (MySearchView) findViewById(R.id.my_search_view);
        this.materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.maintab_search_refresh);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.maintab_search_recyclerView);
        this.searchnull = (TextView) findViewById(R.id.search_null);
        this.back = (FrameLayout) findViewById(R.id.back);
        mListContentAdapter = new ListContentAdapter(SearchDetailActivity.this, mListContents, true, 1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchDetailActivity.this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mListContentAdapter);
    }

    private void closeIME() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onNetChange() {
        if (MyApplication.mNetWorkState != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mListContents != null && mListContents.size() == 0 && inputString != null) {
            materialRefreshLayout.autoRefresh();
        }
    }

    /**
     * 点击搜索获取数据
     */
    private class SearchDataTask extends AsyncTask<String, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(String... strings) {
            List<ListContent> listContents = ((MyApplication) getApplication()).instanceConnect().setContentPost(String.valueOf(readPage),
                    inputString);
            readPage++;
            return listContents;
        }

        @Override
        protected void onPostExecute(List<ListContent> mListContents) {
            super.onPostExecute(mListContents);
            if (mListContents != null && mListContents.size() != 0) {
                mListContentAdapter.setData(mListContents);
                mListContentAdapter.notifyDataSetChanged();
                isNull = false;
            } else {
                isNull = true;
            }
            materialRefreshLayout.finishRefresh();
        }
    }

    /**
     * 刷新数据
     */
    private class RefreshDataTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            List<ListContent> listContents = ((MyApplication) getApplication()).instanceConnect().setContentPost(String.valueOf(readPage),
                    inputString);
            readPage++;
            return listContents;
        }

        @Override
        protected void onPostExecute(List<ListContent> mListContents) {
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
    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            if (mListContentAdapter.getData().size() == 0) {
                return null;
            }
            List<ListContent> listContents = ((MyApplication) getApplication()).instanceConnect().setContentPost(String.valueOf(readPage),
                    inputString);
            readPage++;
            return listContents;
        }

        @Override
        protected void onPostExecute(List<ListContent> ListContents) {
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
