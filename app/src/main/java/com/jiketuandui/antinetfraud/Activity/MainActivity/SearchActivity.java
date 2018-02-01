package com.jiketuandui.antinetfraud.Activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.SQL.RecordSQLiteOpenHelper;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.View.MyListView;
import com.jiketuandui.antinetfraud.View.MySearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends Activity {

    @BindView(R.id.search_clear)
    AppCompatTextView searchClear;
    @BindView(R.id.search_listView)
    MyListView searchListView;
    @BindView(R.id.my_search_view)
    MySearchView mySearchView;
    private RecordSQLiteOpenHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        helper = new RecordSQLiteOpenHelper(SearchActivity.this);
        updateAdapter("");
        initListener();
    }

    private void initListener() {

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

        searchClear.setOnClickListener(view -> {
            helper.deleteData();
            updateAdapter("");
        });

        searchListView.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView tv = view.findViewById(R.id.text_history);
            mySearchView.setInputString(tv.getText().toString());
        });
    }

    private void updateAdapter(String name) {
        Cursor cursor = helper.queryData(name);
        BaseAdapter adapter = new SimpleCursorAdapter(SearchActivity.this,
                R.layout.search_history_list,
                cursor,
                new String[]{"name"},
                new int[]{R.id.text_history},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        searchListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 搜索的动作
     */
    private void searchfunction(String text) {
        if (!text.equals("")) {
            // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
            boolean hasData = helper.hasData(text.trim());
            if (!hasData) {
                helper.insertData(text.trim());
                updateAdapter("");
            }

            Intent intent = new Intent(SearchActivity.this,
                    SearchDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(MyApplication.getInstance().getSEARCHSTRING(), text);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
