package com.jiketuandui.antinetfraud.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.Activity.SearchActivity;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.SQL.RecordSQLiteOpenHelper;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.View.MyListView;
import com.jiketuandui.antinetfraud.View.MySearchView;
import com.jiketuandui.antinetfraud.banner.BannerBaseView;
import com.jiketuandui.antinetfraud.banner.MainBannerView;
import com.jiketuandui.antinetfraud.banner.bean.BaseBannerBean;

import java.util.ArrayList;
import java.util.List;


public class MainTabSearch extends Fragment {

    private MySearchView my_search_view;
    private MainBannerView bannerContent;
    private TextView search_clear;
    private RecordSQLiteOpenHelper helper;
    private MyListView search_listView;
    private BaseAdapter adapter;
    private TextView tv_01;
    private TextView tv_02;

    /**
     * 当前页面的各个Item的数据存放容器
     */
    private List<ListContent> mListContents = new ArrayList<>();
    private List<String> bannerTitle = new ArrayList<>();

    private View.OnClickListener tvListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_01:
                    Toast.makeText(getContext(), "正在抓紧开发中~", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_02:
                    Toast.makeText(getContext(), "正在抓紧开发中~", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new RecordSQLiteOpenHelper(getActivity());

        View view = inflater.inflate(R.layout.main_tab_search, container, false);
        initAllView(view);
        initView();
        return view;
    }

    private void initAllView(View view) {
        this.my_search_view = (MySearchView) view.findViewById(R.id.my_search_view);
        //    bannerContent = (RelativeLayout) view.findViewById(R.id.search_banner_cont);
        this.search_clear = (TextView) view.findViewById(R.id.search_clear);
        this.search_listView = (MyListView) view.findViewById(R.id.search_listView);

        this.tv_01 = (TextView) view.findViewById(R.id.tv_01);
        this.tv_02 = (TextView) view.findViewById(R.id.tv_02);
    }

    private void updateAdapter(String name) {
        Cursor cursor = helper.queryData(name);
        adapter = new SimpleCursorAdapter(getContext(),
                R.layout.search_history_list,
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
        //init_banner();
        initListener();
        updateAdapter("");
    }

    private void init_banner() {
        new initBannerTask().execute();
    }

    private void initListener() {
        tv_01.setOnClickListener(tvListener);
        tv_02.setOnClickListener(tvListener);


        my_search_view.setSearchViewListener(new MySearchView.SearchViewListener() {
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
                my_search_view.setInputString(tv.getText().toString());
            }
        });
    }

    /**
     * 搜索的动作
     * */
    private void searchfunction(String text) {
        if(!text.equals("")) {
            closeIME();
            // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
            boolean hasData = helper.hasData(text.trim());
            if (!hasData) {
                helper.insertData(text.trim());
                updateAdapter("");
            }

            Intent intent = new Intent(getActivity(), SearchActivity.class);
            Bundle mbundle = new Bundle();
            mbundle.putString(Constant.SEARCHSTRING, text);
            intent.putExtras(mbundle);
            startActivity(intent);
        }
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

    private void closeIME() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
