package com.jiketuandui.antinetfraud.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constant;

import java.util.ArrayList;
import java.util.List;


public class MainTabSearch extends Fragment {

    private SearchView searchView;

    private int readPage;
    private MaterialRefreshLayout materialRefreshLayout;
    private RecyclerView mRecyclerView;
    private ListContentAdapter mListContentAdapter;
    private String inputString;
    /**
     * 当前页面的各个Item的数据存放容器
     */
    private List<ListContent> mListContents = new ArrayList<>();

    /**
     * 判断搜索是否加载完成
     * */
    private boolean isSearched = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_search, container, false);
        searchView = (SearchView) view.findViewById(R.id.maintab_search_searchview);
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById
                (R.id.maintab_search_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.maintab_search_recyclerView);
        initView();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        readPage = 1;
        /**
         * Adapter：使用RecyclerView之前，你需要一个继承自RecyclerView.Adapter的适配器，
         * 作用是将数据与每一个item的界面进行绑定。
         * */
        mListContentAdapter = new ListContentAdapter(getActivity(), mListContents);
        /**
         * LayoutManager：用来确定每一个item如何进行排列摆放，何时展示和隐藏。
         * 回收或重用一个View的时候，LayoutManager会向适配器请求新的数据来替换旧的数据，
         * 这种机制避免了创建过多的View和频繁的调用findViewById方法（与ListView原理类似）。
         * */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mListContentAdapter);

        initListener();
    }

    private void initListener() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (isSearched) {
                    new RefreshDataTask().execute();
                } else {
                   materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (isSearched) {
                    new LoadMoreDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });

        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入您要查找的信息");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override// 单击搜索按钮时激活该方法
            public boolean onQueryTextSubmit(String s) {
                isSearched = false;
                return false;
            }

            @Override// 当用户输入字符时激发该方法
            public boolean onQueryTextChange(String s) {
                inputString = s;
                mListContents.clear();
                mListContentAdapter.clearData();
                readPage = 1;
                materialRefreshLayout.autoRefresh();
                new SearchDataTask().execute(s);
                return true;
            }
        });
    }

    /**
     * 点击搜索获取数据
     * */
    class SearchDataTask extends AsyncTask<String, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(String... strings) {
            List<ListContent> listContents = getConnect.setContentPost(String.valueOf(readPage),
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
            isSearched = true;
            materialRefreshLayout.finishRefresh();
        }
    }

    /**
     * 刷新数据
     */
    class RefreshDataTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            List<ListContent> listContents = getConnect.setContentPost(String.valueOf(readPage),
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
    class LoadMoreDataTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            if (mListContentAdapter.getData().size() == 0) {
                return null;
            }
            List<ListContent> listContents = getConnect.setContentPost(String.valueOf(readPage),
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
            if (!Constant.isContainLists(mListContentAdapter,ListContents)) {
                mListContentAdapter.addData(ListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
    }
}
