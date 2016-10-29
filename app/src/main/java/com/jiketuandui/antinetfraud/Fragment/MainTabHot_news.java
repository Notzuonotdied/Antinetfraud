package com.jiketuandui.antinetfraud.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class MainTabHot_news extends Fragment {

    /**
     * 定义当前的ViewIndication的位置
     */
    private int position;
    /**
     * 第几页
     */
    private int readPage;

    private MaterialRefreshLayout materialRefreshLayout;
    private RecyclerView mRecyclerView;
    private ListContentAdapter mListContentAdapter;
    /**
     * 当前页面的各个Item的数据存放容器
     */
    private List<ListContent> mListContents = new ArrayList<>();
    /**
     * 在上拉刷新的时候，判断，是否处于上拉刷新，如果是的话，就禁止在一次刷新，保障在数据加载完成之前
     * 避免重复和多次加载
     */
    private boolean isFirstRefresh = true;
    private boolean isNeedtoRefresh = false;
    private boolean isOpenTop = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 获取当前Item的下标
        this.position = getArguments().getInt(Constant.MAINPAGEPOSITONHOT);
        isOpenTop = position == 0;

        View view = inflater.inflate(R.layout.main_tab_hot_news, null);
        this.materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.maintab_hot_refresh);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.maintab_hot_recyclerView);

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
        mListContentAdapter = new ListContentAdapter(getActivity(), mListContents, isOpenTop, 2);
        /**
         * LayoutManager：用来确定每一个item如何进行排列摆放，何时展示和隐藏。
         * 回收或重用一个View的时候，LayoutManager会向适配器请求新的数据来替换旧的数据，
         * 这种机制避免了创建过多的View和频繁的调用findViewById方法（与ListView原理类似）。
         * */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mListContentAdapter);
        //mRecyclerView.addItemDecoration(new MyItemDecoration());

        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            materialRefreshLayout.autoRefresh();
            isFirstRefresh = false;
        }

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                new RefreshDataTask().execute(position);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                new LoadMoreDataTask().execute(position);
            }
        });
    }

    /**
     * 刷新数据
     */
    class RefreshDataTask extends AsyncTask<Integer, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Integer... params) {
            readPage = 1;
            isNeedtoRefresh = true;
            return getConnect.setContentURLByTagId(getConnect.UrlContentHot,
                    String.valueOf(readPage), String.valueOf(params[0]));
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
    class LoadMoreDataTask extends AsyncTask<Integer, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Integer... params) {
            if (mListContentAdapter.getData().size() == 0) {
                return null;
            }
            List<ListContent> ListContents = getConnect.setContentURLByTagId(getConnect.UrlContentHot,
                    String.valueOf(readPage), String.valueOf(params[0]));
            readPage++;
            isNeedtoRefresh = true;
            return ListContents;
        }

        @Override
        protected void onPostExecute(List<ListContent> ListContents) {
            super.onPostExecute(ListContents);
            if (ListContents == null) {
                if (!isNeedtoRefresh) {
                    Toast.makeText(getContext(), "已到底部~", Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    return;
                }
                isNeedtoRefresh = true;
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
