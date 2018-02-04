package com.jiketuandui.antinetfraud.activity.article;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.RefreshUtil;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

/**
 * 分类文章显示
 *
 * @author wangyu
 */
public class ArticleTagsListActivity extends AppCompatActivity {
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_tags_list);
        RefreshUtil refreshUtil = new RefreshUtil(this);
        refreshUtil.materialRefreshLayout = findViewById(R.id.tags_refresh);
        refreshUtil.tagsRecyclerView = findViewById(R.id.tags_recyclerView);
        refreshUtil.tagsTitle = findViewById(R.id.tags_title);
        refreshUtil.setTagId(getIntent().getExtras().getInt(Constants.TAGS_ID));
        refreshUtil.initViewAttr();
        refreshUtil.setRefreshInterface(new RefreshUtil.RefreshInterface() {
            @Override
            public void refresh() {
                refreshUtil.onRefresh(articleService.getArticleListByTag(
                        refreshUtil.getTagId(), refreshUtil.getReadPage()
                ));
            }

            @Override
            public void loadMore() {
                refreshUtil.onLoadMore(articleService.getArticleListByTag(
                        refreshUtil.getTagId(), refreshUtil.getReadPage()
                ));
            }
        });
    }
}
