package com.jiketuandui.antinetfraud.activity.article;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.RefreshUtil;
import com.jiketuandui.antinetfraud.View.myRecyclerView.MyItemDecoration;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

/**
 * 分类文章显示
 *
 * @author wangyu
 */
public class CommentListActivity extends AppCompatActivity {
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_tags_list);
        Bundle bundle = getIntent().getExtras();
        int articleId = bundle.getInt(Constants.ARTICLE_ID);

        RefreshUtil refreshUtil = new RefreshUtil(this, 1);
        refreshUtil.materialRefreshLayout = findViewById(R.id.tags_refresh);
        refreshUtil.tagsRecyclerView = findViewById(R.id.tags_recyclerView);
        refreshUtil.tagsRecyclerView.addItemDecoration(new MyItemDecoration());
        refreshUtil.initViewAttr();
        refreshUtil.setRefreshInterface(new RefreshUtil.RefreshInterface() {
            @Override
            public void refresh() {
                refreshUtil.onComRefresh(articleService.getCommentList(
                        articleId, refreshUtil.getReadPage()
                ));
            }

            @Override
            public void loadMore() {
                refreshUtil.onComLoadMore(articleService.getCommentList(
                        articleId, refreshUtil.getReadPage()
                ));
            }
        });
    }
}
