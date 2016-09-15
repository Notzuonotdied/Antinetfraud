package com.jiketuandui.antinetfraud.View;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.banner.BannerBaseView;
import com.jiketuandui.antinetfraud.banner.MainBannerView;
import com.jiketuandui.antinetfraud.banner.bean.BaseBannerBean;

import java.util.ArrayList;
import java.util.List;


public class MainTabSearch extends Fragment {

    private SearchView searchView;

    private int readPage;
    private MaterialRefreshLayout materialRefreshLayout;
    private RecyclerView mRecyclerView;
    private ListContentAdapter mListContentAdapter;
    private String inputString;
    private RelativeLayout bannerContent;
    private AppBarLayout search_appBar_Layout;
    /**
     * 当前页面的各个Item的数据存放容器
     */
    private List<ListContent> mListContents = new ArrayList<>();
    private List<String> bannerTitle = new ArrayList<>();

    /**
     * 判断搜索是否加载完成
     * */
    private boolean isSearched = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_tab_search, container, false);
        searchView = (SearchView) view.findViewById(R.id.mainTab_search_searchView);
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById
                (R.id.maintab_search_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.maintab_search_recyclerView);
        bannerContent = (RelativeLayout) view.findViewById(R.id.search_banner_cont);
        search_appBar_Layout = (AppBarLayout) view.findViewById(R.id.search_appBar_Layout);
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

        search_appBar_Layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int total_Height = -search_appBar_Layout.getHeight() + searchView.getHeight();
                if (verticalOffset > total_Height) {
                    Drawable drawable = searchView.getBackground();
                    drawable.setAlpha(changAlpha(Math.abs(verticalOffset), Math.abs(total_Height)));
                }
            }
        });

        initbanner();
        initListener();
    }

    /**
     * 渐变透明度
     *
     * @param heightOffset 高度变化值
     * @param total_Height 总高度
     * */
    private int changAlpha(int heightOffset, int total_Height) {
        float alpha = heightOffset * 255 / total_Height;
        alpha += 188;
        if (alpha > 255) {
            alpha = 255;
        }
        return (int) alpha;
    }

    private void initbanner() {
        new initBannerTask().execute();
    }

    /**
     * 刷新数据
     */
    class initBannerTask extends AsyncTask<Void, Void, List<ListContent>> {

        @Override
        protected List<ListContent> doInBackground(Void... voids) {
            return getConnect.setContentURL(getConnect.UrlContentHot,
                    "1", "3");
        }

        @Override
        protected void onPostExecute(List<ListContent> mListContents) {
            super.onPostExecute(mListContents);
            if (mListContents != null) {
                BannerBaseView banner = new MainBannerView(getActivity());
                List<BaseBannerBean> list = new ArrayList<>();
                for (int i = 0;i < 3; i++) {
                    list.add(new BaseBannerBean(mListContents.get(i).getImagelink()));
                    bannerTitle.add(mListContents.get(i).getTitle());
                }
                bannerContent.addView(banner);
                banner.setBannerTitle(bannerTitle);
                banner.update(list);
            }
        }
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
