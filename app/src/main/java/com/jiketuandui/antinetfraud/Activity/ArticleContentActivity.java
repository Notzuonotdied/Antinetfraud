package com.jiketuandui.antinetfraud.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.HTTP.getImage;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.Util.SharedPManager;
import com.jiketuandui.antinetfraud.View.MarkdownView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


/**
 * 文章内容显示
 * <p>
 * 文章在点击之后会将浏览记录提交到服务器
 */
public class ArticleContentActivity extends AppCompatActivity implements NetBroadcastReceiver.netEventHandler {

    private int reHeight;
    private TextView article_title;
    private TextView article_info;
    private TextView article_time;
    private MarkdownView article_markdownView;
    private TextView article_textView;
    private ArticleContent mArticleContent;
    private SimpleDraweeView head_layout;
    private LinearLayout head_info;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private boolean isLessThan;
    private AppBarLayout app_bar_layout;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private boolean isCollected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // drawable参数设为null，表示小菊花的样式随系统默认
        mProgressBar = createProgressBar(ArticleContentActivity.this, null);
        setContentView(R.layout.activity_article_content);

        isCollected = false; // false表示没有被收藏，true表示被收藏
        // 注册
        NetBroadcastReceiver.mListeners.add(this);
        // 读取文章
        LoadingArticle();
        // 初始化View
        initView();
        // 初始化监听事件
        initListener();
        // 初始化悬浮按钮
        initFloatButton();
    }

    @Override
    public void onNetChange() {
        if (MyApplication.getInstance().getmNetWorkState() != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mArticleContent == null) {
            LoadingArticle();
        }
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
                    if (mArticleContent != null) {
                        mCollapsingToolbarLayout.setTitle(mArticleContent.getTitle());
                    }
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
        // -----------------------
        //article_textView = (TextView) findViewById(R.id.article_textView);
        // -----------------------
        head_info = (LinearLayout) findViewById(R.id.head_info);
        //head_layout = (LinearLayout) findViewById(R.id.head_layout);
        head_layout = (SimpleDraweeView) findViewById(R.id.head_layout);
        // 定义收缩栏
        app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
    }

    /**
     * 初始化悬浮按钮并为悬浮按钮设置点击事件
     */
    private void initFloatButton() {
        // 悬浮按钮
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.mipmap.home);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView itemIconP = new ImageView(this);
        itemIconP.setImageResource(R.drawable.button_praise_selector);
        SubActionButton praise = itemBuilder.setContentView(itemIconP).build();
        praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncPraise().execute(mArticleContent.getId());
            }
        });
        ImageView itemIconC = new ImageView(this);
        itemIconC.setImageResource(R.drawable.button_collect_selector);
        SubActionButton collect = itemBuilder.setContentView(itemIconC).build();
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncCollect().execute(mArticleContent.getId());
            }
        });
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(praise, 100, 100)
                .addSubActionView(collect, 100, 100)
                .attachTo(actionButton)
                .build();
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
        int articleId = this.getIntent().getExtras().getInt(MyApplication.getInstance().getCONTENTID());
        new LoadArticle().execute(articleId);
    }

    /**
     * 设置View一些内容
     */
    private void initAppBarLayout(ArticleContent articleContent) {
        if (articleContent == null) {
            return;
        }
        mArticleContent = articleContent;
        article_title.setText(mArticleContent.getTitle());
        article_info.setText(mArticleContent.getInfo());
        article_time.setText(mArticleContent.getCreatetime());
        article_markdownView.loadMarkdown(articleContent.getContent());
 //       article_textView.setText(Html.fromHtml(articleContent.getContent()));
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

//    /**
//     * 回收内存
//     */
//    @Override
//    protected void onDestroy() {
//        if (head_layout != null && head_layout.getDrawingCache() != null ||
//                mCollapsingToolbarLayout != null && mCollapsingToolbarLayout.getDrawingCache() != null) {
//            Bitmap bitmap = head_layout.getDrawingCache();
//            Bitmap bitmap_coo = mCollapsingToolbarLayout.getDrawingCache();
//            if (Build.VERSION.SDK_INT >= 16) {
//                head_layout.setBackground(null);
//            } else {
//                head_layout.setBackgroundDrawable(null);
//            }
//            mCollapsingToolbarLayout.setContentScrim(null);
//            if (bitmap != null && !bitmap.isRecycled()) {
//                bitmap.recycle();
//            }
//            if (bitmap_coo != null && !bitmap_coo.isRecycled()) {
//                bitmap_coo.recycle();
//            }
//            System.gc();
//        }
//        super.onDestroy();
//    }

    /**
     * 在屏幕上添加一个转动的小菊花（传说中的Loading），默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则小菊花将会处于最底层，被屏幕其他View给覆盖
     *
     * @param activity                    需要添加菊花的Activity
     * @param customIndeterminateDrawable 自定义的菊花图片，可以为null，此时为系统默认菊花
     * @return {ProgressBar}    菊花对象
     */
    private ProgressBar createProgressBar(Activity activity, Drawable customIndeterminateDrawable) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null) {
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);
        return progressBar;
    }

    private class LoadArticle extends AsyncTask<Integer, Integer, ArticleContent> {

        private String mUid = null;

        @Override
        protected void onProgressUpdate(Integer... values) {
            //mProgressBar.setProgress(values[0]);// 每次更新进度条
        }

        @Override
        protected void onPreExecute() {

            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override// 在子线程执行
        protected ArticleContent doInBackground(Integer... integers) {
            ArticleContent mAContent = ((MyApplication) getApplication()).instanceConnect().setArticleURL(integers[0]);
            if (mAContent != null) {
                mAContent.setBitmap(getImage.getBitmap(mAContent,
                        ((MyApplication) getApplication()).getMyScreenWidth(),
                        reHeight, ArticleContentActivity.this));
            }
            // 判断用户是否登陆，假如登陆了，就提交用户的浏览历史
            if (isLogin() && mAContent != null) {
                ((MyApplication) getApplication()).instancepostAccount()
                        .postReadHistory("uid=" + mUid + "&article_id=" + mAContent.getId());
            }
            if (mAContent != null) {
                isCollected = ((MyApplication) getApplication()).isContain(mAContent.getId());
            }
            return mAContent;
        }

        @Override// 在主线程执行
        protected void onPostExecute(ArticleContent articleContent) {
            mProgressBar.setVisibility(View.GONE);
            initAppBarLayout(articleContent);
        }

        /**
         * 判断是否登陆，并且存在uid
         */
        private boolean isLogin() {
            SharedPManager sharedPManager = new SharedPManager(ArticleContentActivity.this);
            if (sharedPManager.isContains(MyApplication.getInstance().getUid())) {
                mUid = sharedPManager.getString(MyApplication.getInstance().getUid(), "0");
                Log.i("Notzuonotdied", "mUid = " + mUid);
                return true;
            }
            return false;
        }
    }

    private class AsyncPraise extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            return ((MyApplication) getApplication()).instancePraise().setPraiseGet(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                mArticleContent.setPraise(String.valueOf(Integer.valueOf(mArticleContent.getPraise()) + 1));
                article_info.setText(mArticleContent.getInfo());
                Toast.makeText(ArticleContentActivity.this, "点赞成功~", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aBoolean);
        }
    }

    private class AsyncCollect extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            // false表示没有被收藏，true表示被收藏
            if (isCollected) {// true
                isCollected = false;
                return ((MyApplication) getApplication()).instancepostAccount().postCancleCollection(
                        "uid=" + new SharedPManager(ArticleContentActivity.this)
                                .getString(MyApplication.getInstance().getUid(), "0")
                                + "&&article_id=" + strings[0]);
            } else {// false
                isCollected = true;
                return ((MyApplication) getApplication()).instancepostAccount().postCollection(
                        "uid=" + new SharedPManager(ArticleContentActivity.this)
                                .getString(MyApplication.getInstance().getUid(), "0")
                                + "&&article_id=" + strings[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean && !isCollected) {
                Toast.makeText(ArticleContentActivity.this, "取消收藏成功~", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ArticleContentActivity.this, "收藏成功~", Toast.LENGTH_SHORT).show();
            }
            // 重置判断缓存
            ((MyApplication) getApplication()).initCache();
            super.onPostExecute(aBoolean);
        }
    }
}
