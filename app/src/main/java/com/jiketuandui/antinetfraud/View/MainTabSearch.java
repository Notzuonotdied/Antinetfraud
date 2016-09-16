package com.jiketuandui.antinetfraud.View;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.SQL.RecordSQLiteOpenHelper;
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
    private ScrollView search_scroll;
    private TextView search_clear;
    private RecordSQLiteOpenHelper helper;
    private MyListView search_listView;
    private BaseAdapter adapter;
    private TextView search_null;
    private TextView tv_01;
    private TextView tv_02;
    private TextView tv_03;
    private TextView tv_04;
    /**
     * 当前页面的各个Item的数据存放容器
     */
    private List<ListContent> mListContents = new ArrayList<>();
    private List<String> bannerTitle = new ArrayList<>();

    /**
     * 判断搜索是否加载完成
     */
    private boolean isSearched = false;
    private boolean isNull = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new RecordSQLiteOpenHelper(getActivity());

        View view = inflater.inflate(R.layout.main_tab_search, container, false);
        this.searchView = (SearchView) view.findViewById(R.id.mainTab_search_searchView);
        this.materialRefreshLayout = (MaterialRefreshLayout) view.findViewById
                (R.id.maintab_search_refresh);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.maintab_search_recyclerView);
        this.search_scroll = (ScrollView) view.findViewById(R.id.search_scroll);
        //    bannerContent = (RelativeLayout) view.findViewById(R.id.search_banner_cont);
        this.search_clear = (TextView) view.findViewById(R.id.search_clear);
        this.search_listView = (MyListView) view.findViewById(R.id.search_listView);
        this.search_null = (TextView) view.findViewById(R.id.search_null);
        this.tv_04 = (TextView) view.findViewById(R.id.tv_04);
        this.tv_03 = (TextView) view.findViewById(R.id.tv_03);
        this.tv_02 = (TextView) view.findViewById(R.id.tv_02);
        this.tv_01 = (TextView) view.findViewById(R.id.tv_01);
        initView();
        return view;
    }

    private void updateAdapter(String name) {
        Cursor cursor = helper.queryData(name);
        adapter = new SimpleCursorAdapter(getContext(),
                R.layout.my_list_view,
                cursor,
                new String[]{"name"},
                new int[]{R.id.text_history},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        search_listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        readPage = 1;
        mListContentAdapter = new ListContentAdapter(getActivity(), mListContents);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mListContentAdapter);

        //init_banner();
        initListener();
        updateAdapter("");
    }

    /**
     * 渐变透明度
     *
     * @param heightOffset 高度变化值
     * @param total_Height 总高度
     */
    private int changAlpha(int heightOffset, int total_Height) {
        float alpha = heightOffset * 255 / total_Height;
        alpha += 108;
        if (alpha > 255) {
            alpha = 255;
        }
        return (int) alpha;
    }


    private void init_banner() {
        new initBannerTask().execute();
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
                inputString = s;
                mListContents.clear();
                mListContentAdapter.clearData();
                readPage = 1;
                materialRefreshLayout.autoRefresh();

                new SearchDataTask().execute(s);

                if (s.equals("")) {
                    search_scroll.setVisibility(View.VISIBLE);
                    search_null.setVisibility(View.GONE);
                    materialRefreshLayout.setVisibility(View.GONE);
                } else {
                    if (isNull) {
                        search_null.setVisibility(View.VISIBLE);
                        search_scroll.setVisibility(View.GONE);
                    } else {
                        search_scroll.setVisibility(View.VISIBLE);
                        search_null.setVisibility(View.GONE);
                    }
                    materialRefreshLayout.setVisibility(View.VISIBLE);
                }

                isSearched = false;
                searchView.clearFocus();
                // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                boolean hasData = helper.hasData(s.trim());
                if (!hasData) {
                    helper.insertData(s.trim());
                    updateAdapter("");
                }

                return false;
            }

            @Override// 当用户输入字符时激发该方法
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    search_null.setVisibility(View.GONE);
                    search_scroll.setVisibility(View.VISIBLE);
                    materialRefreshLayout.setVisibility(View.GONE);
                }
                //else {
//                    search_scroll.setVisibility(View.GONE);
//                    materialRefreshLayout.setVisibility(View.VISIBLE);
//                }

                return true;
            }
        });

        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.deleteData();
                updateAdapter("");
            }
        });

        search_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.text_history);
                String name = tv.getText().toString();
                searchView.setQuery(name, false);
            }
        });
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
                for (int i = 0; i < 3; i++) {
                    list.add(new BaseBannerBean(mListContents.get(i).getImagelink()));
                    bannerTitle.add(mListContents.get(i).getTitle());
                }
                bannerContent.addView(banner);
                banner.setBannerTitle(bannerTitle);
                banner.update(list);
            }
        }
    }

    /**
     * 点击搜索获取数据
     */
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
                isNull = false;
            } else {
                isNull = true;
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
            if (!Constant.isContainLists(mListContentAdapter, ListContents)) {
                mListContentAdapter.addData(ListContents);
                mListContentAdapter.notifyDataSetChanged();
            }
            materialRefreshLayout.finishRefreshLoadMore();
        }
    }
}
