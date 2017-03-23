package com.jiketuandui.antinetfraud.Activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.SQL.RecordSQLiteOpenHelper;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.View.MyListView;
import com.jiketuandui.antinetfraud.View.MySearchView;

public class SearchActivity extends Activity {

    private android.widget.TextView searchclear;
    private com.jiketuandui.antinetfraud.View.MyListView searchlistView;
    private MySearchView my_search_view;
    private RecordSQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        helper = new RecordSQLiteOpenHelper(SearchActivity.this);

        initView();
        initLintener();
    }

    private void initLintener() {

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

        searchclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.deleteData();
                updateAdapter("");
            }
        });

        searchlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.text_history);
                my_search_view.setInputString(tv.getText().toString());
            }
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
        searchlistView.setAdapter(adapter);
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
            Bundle mbundle = new Bundle();
            mbundle.putString(MyApplication.getInstance().getSEARCHSTRING(), text);
            intent.putExtras(mbundle);
            startActivity(intent);
        }
    }

    private void initView() {
        this.searchlistView = (MyListView) findViewById(R.id.search_listView);
        this.searchclear = (TextView) findViewById(R.id.search_clear);
        this.my_search_view = (MySearchView) findViewById(R.id.my_search_view);

        updateAdapter("");
    }
}
