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
import com.jiketuandui.antinetfraud.Holder.MyItemDecoration;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Notzuonotdied on 2016/8/1.
 * 这个是放置主页新闻ViewPage的内容的
 */
public class MainTab_news extends Fragment  implements NetBroadcastReceiver.netEventHandler {

    /**
     * 定义当前的ViewIndication的位置
     */
    private int position;
    /**
     * 当前加载的页数
     */
    private int readPage;

    private List<ListContent> mListContents = new ArrayList<>();
    private MaterialRefreshLayout materialRefreshLayout;
    private RecyclerView mRecyclerView;
    private ListContentAdapter mListContentAdapter;
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
        this.position = getArguments().getInt(MyApplication.MAINPAGEPOSITON);
        isOpenTop = position == 0;

        View view = inflater.inflate(R.layout.main_tab_news, null);
        this.materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.maintab_news_refresh);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.maintab_news_recyclerView);

        initView();
        initData();

        // 注册
        NetBroadcastReceiver.mListeners.add(this);

        return view;
    }

    private void initData() {

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
    private void initView() {
        readPage = 1;
        /**
         * Adapter：使用RecyclerView之前，你需要一个继承自RecyclerView.Adapter的适配器，
         * 作用是将数据与每一个item的界面进行绑定。
         * */
        mListContentAdapter = new ListContentAdapter(getActivity(), mListContents, isOpenTop, 1);
        /**
         * LayoutManager：用来确定每一个item如何进行排列摆放，何时展示和隐藏。
         * 回收或重用一个View的时候，LayoutManager会向适配器请求新的数据来替换旧的数据，
         * 这种机制避免了创建过多的View和频繁的调用findViewById方法（与ListView原理类似）。
         * */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mListContentAdapter);
        mRecyclerView.addItemDecoration(new MyItemDecoration());

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
                    new RefreshDataTask().execute(position);
                } else {
                    materialRefreshLayout.finishRefresh();
                    isFirstRefresh = true;
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(getContext())) {
                    new LoadMoreDataTask().execute(position);
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    @Override
    public void onNetChange() {
        if (MyApplication.mNetWorkState != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mListContents != null && mListContents.size() == 0) {
            materialRefreshLayout.autoRefresh();
        }
    }

    /**
     * 刷新数据
     */
    private class RefreshDataTask extends AsyncTask<Integer, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Integer... params) {
            readPage = 1;
            return ((MyApplication) getActivity().getApplication())
                    .instanceConnect().setContentURLByTagId(getConnect.UrlContentHead,
                            String.valueOf(readPage), String.valueOf(params[0]));
        }

        @Override
        protected void onPostExecute(List<ListContent> ListContents) {
            super.onPostExecute(ListContents);
            if (ListContents != null) {
                isNeedtoRefresh = true;
                mListContentAdapter.setData(ListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            materialRefreshLayout.finishRefresh();
        }
    }

    /**
     * 加载更多数据
     */
    private class LoadMoreDataTask extends AsyncTask<Integer, Void, List<ListContent>> {
        @Override
        protected List<ListContent> doInBackground(Integer... params) {
            List<ListContent> ListContents;
            if (mListContentAdapter.getData().size() == 0) {
                return null;
            }
            ListContents = ((MyApplication) getActivity().getApplication())
                    .instanceConnect().setContentURLByTagId(getConnect.UrlContentHead,
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
            if (!MyApplication.isContainLists(mListContentAdapter, ListContents)) {
                mListContentAdapter.addData(ListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
    }


}
