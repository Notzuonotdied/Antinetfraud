package com.jiketuandui.antinetfraud.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.HTTP.getImage;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.View.MarkdownView;

public class ArticleContentActivity extends AppCompatActivity {

    private int reHeight;
    private TextView article_title;
    private TextView article_info;
    private TextView article_time;
    private MarkdownView article_markdownView;
    private ArticleContent mArticleContent;
    //private LinearLayout head_layout;
    private SimpleDraweeView head_layout;
    private LinearLayout head_info;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private boolean isLessThan;
    private AppBarLayout app_bar_layout;
    private Toolbar mToolbar;
    // 定义进度条
    private ProgressBar mProgressBar;
    private Drawable drawable_head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        // 读取文章
        LoadingArticle();
        // 初始化View
        initView();
        initListener();
    }

    private void initListener() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 返回键
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        reHeight = head_layout.getHeight();
        isLessThan = false;
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.color_white));
        // 设置标题
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int total_Height = -mCollapsingToolbarLayout.getHeight() + mToolbar.getHeight();
                if (verticalOffset <= total_Height) {
                    mCollapsingToolbarLayout.setTitle(mArticleContent.getTitle());
                    head_info.setVisibility(View.GONE);
                    if (!isLessThan) {
                        mCollapsingToolbarLayout.setContentScrimColor(0xffffffff);
                        mCollapsingToolbarLayout.setCollapsedTitleTextColor(0xff717171);
                    }
                    isLessThan = true;
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                    head_info.setVisibility(View.VISIBLE);
                    Drawable drawable = head_info.getBackground();
                    drawable.setAlpha(changAlpha(Math.abs(verticalOffset), Math.abs(total_Height)));
                    // 设置头图
                    if (Build.VERSION.SDK_INT >= 16) {
                        head_info.setBackground(drawable);
                    } else {
                        head_info.setBackgroundDrawable(drawable);
                    }
                    if (isLessThan) {
                        mCollapsingToolbarLayout.setContentScrim(null);
                    }
                    isLessThan = false;
                }
            }
        });
    }

    /**
     * 这里包含了标签的部分,暂时未实现
     */
    private void initView() {
        article_title = (TextView) findViewById(R.id.article_title);
        article_info = (TextView) findViewById(R.id.article_info);
        article_time = (TextView) findViewById(R.id.article_time);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        article_markdownView = (MarkdownView) findViewById(R.id.article_markdownView);
        head_info = (LinearLayout) findViewById(R.id.head_info);
        //head_layout = (LinearLayout) findViewById(R.id.head_layout);
        head_layout = (SimpleDraweeView) findViewById(R.id.head_layout);
        // 定义收缩栏
        app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
    }

    /**
     * 渐变透明度
     *
     * @param heightOffset 高度变化值
     * @param total_Height 总高度
     */
    private int changAlpha(int heightOffset, int total_Height) {
        float alpha = heightOffset * 255 / total_Height;
        if (alpha > 255) {
            alpha = 255;
        }
        return (int) (255 - alpha);
    }

    /**
     * 获取文章内容
     */
    private void LoadingArticle() {
        //mProgressBar = (ProgressBar) findViewById(R.id.article_progress);

        // 根据ID获取文章的内容
        int articleId = this.getIntent().getExtras().getInt(Constant.CONTENTID);
        new LoadArticle().execute(articleId);
    }

    /**
     * 设置View一些内容
     */
    private void initAppBarLayout(ArticleContent articleContent) {
        mArticleContent = articleContent;
        article_title.setText(mArticleContent.getTitle());
        article_info.setText(mArticleContent.getInfo());
        article_time.setText(mArticleContent.getCreatetime());

        article_markdownView.loadMarkdown(articleContent.getContent());

        //       head_layout.setImageURI(mArticleContent.getAllImagelink());
        // 设置头图
        if (Build.VERSION.SDK_INT >= 16) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(mArticleContent.getImagelink())
                    .setTapToRetryEnabled(true)
                    .setOldController(head_layout.getController())
                    .build();
            head_layout.setController(controller);
//            head_layout.setBackground(new BitmapDrawable(
//                    mArticleContent.getBitmap()));
        } else {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(mArticleContent.getImagelink())
                    .setTapToRetryEnabled(true)
                    .setOldController(head_layout.getController())
                    .build();
            head_layout.setController(controller);
//            head_layout.setBackgroundDrawable(new BitmapDrawable(
//                    mArticleContent.getBitmap()));
        }

//        drawable_head = Build.VERSION.SDK_INT > 16 ? new BitmapDrawable(
//                BlurUtilForHighAPI.Highblur(mArticleContent.getBitmap(),
//                        25f, ArticleContentActivity.this)) : new BitmapDrawable(
//                BlurUtil.fastblur(mArticleContent.getBitmap(), 66));
//        mCollapsingToolbarLayout.setContentScrim(drawable_head);
    }

    /**
     * 回收内存
     */
    @Override
    protected void onDestroy() {
        if (head_layout != null && head_layout.getDrawingCache() != null ||
                mCollapsingToolbarLayout != null && mCollapsingToolbarLayout.getDrawingCache() != null) {
            Bitmap bitmap = head_layout.getDrawingCache();
            Bitmap bitmap_coo = mCollapsingToolbarLayout.getDrawingCache();
            if (Build.VERSION.SDK_INT >= 16) {
                head_layout.setBackground(null);
            } else {
                head_layout.setBackgroundDrawable(null);
            }
            mCollapsingToolbarLayout.setContentScrim(null);
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (bitmap_coo != null && !bitmap_coo.isRecycled()) {
                bitmap_coo.recycle();
            }
            System.gc();
        }
        super.onDestroy();
    }

    class LoadArticle extends AsyncTask<Integer, Integer, ArticleContent> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            //mProgressBar.setProgress(values[0]);// 每次更新进度条
        }

        @Override
        protected void onPreExecute() {

            //mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override// 在子线程执行
        protected ArticleContent doInBackground(Integer... integers) {
            ArticleContent mAContent = getConnect.setArticleURL(integers[0]);
            if (mAContent != null) {
                mAContent.setBitmap(getImage.getBitmap(mAContent,
                        ((MyApplication) getApplication()).getMyScreenWidth(),
                        reHeight, ArticleContentActivity.this));
            }
            return mAContent;
        }

        @Override// 在主线程执行
        protected void onPostExecute(ArticleContent articleContent) {
            //mProgressBar.setVisibility(View.GONE);
            initAppBarLayout(articleContent);
        }
    }
}
