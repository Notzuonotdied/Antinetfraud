package com.jiketuandui.antinetfraud.Activity.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.SQL.RecordSQLiteOpenHelper;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.RefreshUtil;
import com.jiketuandui.antinetfraud.View.MySearchView;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索详情页面
 *
 * @author wangyu
 */
public class SearchDetailActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEventHandler {
    @BindView(R.id.my_search_view)
    MySearchView mySearchView;
    @BindView(R.id.search_null)
    AppCompatTextView searchNull;
    private String inputString;
    private RecordSQLiteOpenHelper helper;
    private boolean isNull = false;

    private RefreshUtil refreshUtil;
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search);
        ButterKnife.bind(this);

        helper = new RecordSQLiteOpenHelper(SearchDetailActivity.this);
        inputString = getIntent().getExtras().getString(Constants.SEARCH_STRING);

        refreshUtil = new RefreshUtil(this);
        refreshUtil.materialRefreshLayout = findViewById(R.id.refresh);
        refreshUtil.tagsRecyclerView = findViewById(R.id.recyclerView);
        refreshUtil.setOpenTop(true);
        refreshUtil.initViewAttr();
        refreshUtil.setRefreshInterface(new RefreshUtil.RefreshInterface() {
            @Override
            public void refresh() {
                refreshUtil.onRefresh(articleService.search(inputString,
                        refreshUtil.getReadPage()));
            }

            @Override
            public void loadMore() {
                refreshUtil.onLoadMore(articleService.search(inputString,
                        refreshUtil.getReadPage()));
            }
        });
        refreshUtil.setResponse(new RefreshUtil.Response() {
            @Override
            public void onSuccess() {
                // 搜索结果不为空
                isNull = false;
            }

            @Override
            public void onFailure() {
                // 搜索结果为空
                isNull = true;
            }
        });

        initListener();
        if (!"".equals(inputString)) {
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
    }

    /**
     * 搜索执行的函数
     */
    private void searchFunction(String text) {
        inputString = text;
        refreshUtil.clearData();
        // TODO 搜索
        refreshUtil.onRefresh(articleService.search(inputString,
                refreshUtil.getReadPage()));

        if (!"".equals(text)) {
            if (isNull) {
                searchNull.setVisibility(View.VISIBLE);
                refreshUtil.setGone();
            } else {
                refreshUtil.setVisible();
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


    private void closeIME() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onNetChange() {
        refreshUtil.onNetChangeOnSearch(inputString);
    }
}
