package com.jiketuandui.antinetfraud.Activity.UtilActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.ToolBarLayout;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToTagsListActivity extends AppCompatActivity {
    @BindView(R.id.tags_title)
    ToolBarLayout tagsTitle;
    @BindView(R.id.tags_recyclerView)
    RecyclerView tagsRecyclerView;
    @BindView(R.id.tags_refresh)
    MaterialRefreshLayout tagsRefresh;
    private int readPage;
    private int tagId;
    private int category;
    private boolean isFirstRefresh = true;
    private boolean isNeedtoRefresh = false;
    private ListContentAdapter mListContentAdapter;
    private List<ArticleList.DataBean> mListContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_tags_list);
        ButterKnife.bind(this);
        readPage = 1;
        tagId = getIntent().getExtras().getInt(MyApplication.getInstance().getTAGSID());
        category = getIntent().getExtras().getInt(MyApplication.getInstance().getCATEGORY());
        initView();
        initListener();
    }

    private void initListener() {

        tagsRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefreshLoadMore();
                if (NetWorkUtils.isConnectNET(ToTagsListActivity.this)) {
//                    new RefreshDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
                if (NetWorkUtils.isConnectNET(ToTagsListActivity.this)) {
//                    new LoadMoreDataTask().execute();
                } else {
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void initView() {
        this.mListContentAdapter = new ListContentAdapter(ToTagsListActivity.this, mListContents, false);
        this.tagsRecyclerView.setLayoutManager(new LinearLayoutManager(ToTagsListActivity.this,
                LinearLayoutManager.VERTICAL, false));
        this.tagsRecyclerView.setAdapter(mListContentAdapter);
        // 如果是第一次刷新就启动一次刷新
        if (isFirstRefresh) {
            tagsRefresh.autoRefresh();
            isFirstRefresh = false;
        }
        this.tagsTitle.setText(MyApplication.getInstance().getTabBigTitle()[tagId]);
    }

    /**
     * 刷新数据
     */
//    class RefreshDataTask extends AsyncTask<Void, Void, List<ListContent>> {
//
//        @Override
//        protected List<ListContent> doInBackground(Void... voids) {
//            readPage = 1;
//            //　根据文章类型判断应该获取哪些数据
//            switch (category) {
//                case 1:
//                    mListContents = ((MyApplication) getApplication()).
//                            instanceConnect().setContentURLByTagId(getConnect.UrlContentHead,
//                            String.valueOf(readPage), String.valueOf(tagId));
//                    break;
//                case 2:
//                    mListContents = ((MyApplication) getApplication()).
//                            instanceConnect().setContentURLByTagId(getConnect.UrlContentHot,
//                            String.valueOf(readPage), String.valueOf(tagId));
//                    break;
//            }
//
//            return mListContents;
//        }
//
//        @Override
//        protected void onPostExecute(List<ListContent> mListContents) {
//            super.onPostExecute(mListContents);
//            if (mListContents != null) {
//                isNeedtoRefresh = true;
//                mListContentAdapter.setData(mListContents);
//                mListContentAdapter.notifyDataSetChanged();
//            }
//            tagsRefresh.finishRefresh();
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
//            List<ListContent> ListContents = null;
//            if (mListContentAdapter.getData().size() == 0) {
//                return null;
//            }
//            readPage++;
//            isNeedtoRefresh = true;
//            switch (category) {
//                case 1:
//                    ListContents = ((MyApplication) getApplication()).instanceConnect().setContentURLByTagId(getConnect.UrlContentHead,
//                            String.valueOf(readPage), String.valueOf(tagId));
//                    break;
//                case 2:
//                    ListContents = ((MyApplication) getApplication()).instanceConnect().setContentURLByTagId(getConnect.UrlContentHot,
//                            String.valueOf(readPage), String.valueOf(tagId));
//                    break;
//            }
//            return ListContents;
//        }
//
//        @Override
//        protected void onPostExecute(List<ListContent> ListContents) {
//            super.onPostExecute(ListContents);
//            if (ListContents == null) {
//                if (!isNeedtoRefresh) {
//                    Toast.makeText(ToTagsListActivity.this, "已到底部~", Toast.LENGTH_SHORT).show();
//                    tagsRefresh.finishRefreshLoadMore();
//                    return;
//                }
//                isNeedtoRefresh = true;
//                tagsRefresh.finishRefreshLoadMore();
//                return;
//            }
//            if (!MyApplication.getInstance().isContainLists(mListContentAdapter, ListContents)) {
//                mListContentAdapter.addData(ListContents);
//                mListContentAdapter.notifyDataSetChanged();
//            }
//            tagsRefresh.finishRefreshLoadMore();
//        }
//    }
}
