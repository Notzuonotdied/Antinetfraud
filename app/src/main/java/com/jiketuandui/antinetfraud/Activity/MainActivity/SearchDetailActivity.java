package com.jiketuandui.antinetfraud.Activity.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.SQL.RecordSQLiteOpenHelper;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.MySearchView;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchDetailActivity extends AppCompatActivity implements NetBroadcastReceiver.netEventHandler {
    @BindView(R.id.my_search_view)
    MySearchView mySearchView;
    @BindView(R.id.search_null)
    AppCompatTextView searchNull;
    @BindView(R.id.maintab_search_refresh)
    MaterialRefreshLayout materialRefreshLayout;
    private int readPage;
    private ListContentAdapter mListContentAdapter;
    private String inputString;
    private RecordSQLiteOpenHelper helper;
    private List<ArticleList.DataBean> mListContents = new ArrayList<>();
    private boolean isNull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search);
        // 注册
        NetBroadcastReceiver.mListeners.add(this);
        ButterKnife.bind(this);

        helper = new RecordSQLiteOpenHelper(SearchDetailActivity.this);
        inputString = getIntent().getExtras().getString(MyApplication.getInstance().getSEARCHSTRING());
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
        mySearchView.setSearchViewListener(new MySearchView.SearchViewListener() {
            @Override
            public void onSearch(String text) {
                searchFunction(text);
            }

            @Override
            public void onQueryTextSubmit(String text) {
                searchFunction(text);
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
//                    new RefreshDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(SearchDetailActivity.this)) {
//                    new LoadMoreDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 搜索执行的函数
     */
    private void searchFunction(String text) {
        inputString = text;
        mListContents.clear();
        mListContentAdapter.notifyDataSetChanged();
        readPage = 1;
//        new SearchDataTask().execute(text);

        if (!text.equals("")) {
            if (isNull) {
                searchNull.setVisibility(View.VISIBLE);
                materialRefreshLayout.setVisibility(View.GONE);
            } else {
                materialRefreshLayout.setVisibility(View.VISIBLE);
                searchNull.setVisibility(View.GONE);
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
        mListContentAdapter = new ListContentAdapter(SearchDetailActivity.this, mListContents, true, 1);
        RecyclerView mRecyclerView = findViewById(R.id.maintab_search_recyclerView);
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
        if (MyApplication.getInstance().getNetWorkState() != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mListContents != null && mListContents.size() == 0 && inputString != null) {
            materialRefreshLayout.autoRefresh();
        }
    }

    /**
     * 点击搜索获取数据
     */
//    private class SearchDataTask extends AsyncTask<String, Void, List<ListContent>> {
//        @Override
//        protected List<ListContent> doInBackground(String... strings) {
//            List<ListContent> listContents = ((MyApplication) getApplication())
//                    .instanceConnect().setContentPost(String.valueOf(readPage), inputString);
//            readPage++;
//            return listContents;
//        }
//
//        @Override
//        protected void onPostExecute(List<ListContent> mListContents) {
//            super.onPostExecute(mListContents);
//            if (mListContents != null && mListContents.size() != 0) {
//                mListContentAdapter.setData(mListContents);
//                mListContentAdapter.notifyDataSetChanged();
//                isNull = false;
//            } else {
//                isNull = true;
//            }
//            materialRefreshLayout.finishRefresh();
//        }
//    }
//
//    /**
//     * 刷新数据
//     */
//    private class RefreshDataTask extends AsyncTask<Void, Void, List<ListContent>> {
//
//        @Override
//        protected List<ListContent> doInBackground(Void... voids) {
//            List<ListContent> listContents = ((MyApplication) getApplication()).instanceConnect().setContentPost(String.valueOf(readPage),
//                    inputString);
//            readPage++;
//            return listContents;
//        }
//
//        @Override
//        protected void onPostExecute(List<ListContent> mListContents) {
//            super.onPostExecute(mListContents);
//            if (mListContents != null) {
//                mListContentAdapter.setData(mListContents);
//                mListContentAdapter.notifyDataSetChanged();
//            }
//            materialRefreshLayout.finishRefresh();
//        }
//    }
//
//    /**
//     * 加载更多数据
//     */
//    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<ListContent>> {
//
//        @Override
//        protected List<ListContent> doInBackground(Void... voids) {
//            if (mListContentAdapter.getData().size() == 0) {
//                return null;
//            }
//            List<ListContent> listContents = ((MyApplication) getApplication()).instanceConnect().setContentPost(String.valueOf(readPage),
//                    inputString);
//            readPage++;
//            return listContents;
//        }
//
//        @Override
//        protected void onPostExecute(List<ListContent> ListContents) {
//            super.onPostExecute(ListContents);
//            if (ListContents == null) {
//                materialRefreshLayout.finishRefreshLoadMore();
//                return;
//            }
//            if (!MyApplication.getInstance().isContainLists(mListContentAdapter, ListContents)) {
//                mListContentAdapter.addData(ListContents);
//                mListContentAdapter.notifyDataSetChanged();
//            }
//            materialRefreshLayout.finishRefreshLoadMore();
//        }
//    }
}
