package com.jiketuandui.antinetfraud.Activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Adapter.ArticleDetailAdapter;
import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.Fragment.ArticleDetailFragment.ArticleFragment;
import com.jiketuandui.antinetfraud.Fragment.ArticleDetailFragment.CommentFragment;
import com.jiketuandui.antinetfraud.HTTP.getImage;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.Util.SharedPManager;
import com.jiketuandui.antinetfraud.View.WrapContentHeightViewPager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文章内容显示
 * 文章在点击之后会将浏览记录提交到服务器
 */
public class ArticleContentActivity extends AppCompatActivity
        implements NetBroadcastReceiver.netEventHandler {

    @BindView(R.id.article_title)
    TextView article_title;
    @BindView(R.id.article_info)
    TextView article_info;
    @BindView(R.id.article_time)
    TextView article_time;
    @BindView(R.id.head_info)
    LinearLayout head_info;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;
    @BindView(R.id.head_layout)
    SimpleDraweeView head_layout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private int reHeight;
    //    private MarkdownView article_markdownView;
    //    private TextView article_textView;
    private ArticleContent mArticleContent;
    private boolean isLessThan;
    private boolean isCollected;
    /*这是文章ID*/
    private int articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        ButterKnife.bind(this);

        // 根据ID获取文章的内容
        articleId = this.getIntent().getExtras().getInt(MyApplication.getInstance().getCONTENTID());
        Log.i("Notzuonotdied", "articleId = " + articleId);
        isCollected = false; // false表示没有被收藏，true表示被收藏
        // 注册
        NetBroadcastReceiver.mListeners.add(this);
        // 读取文章
        LoadingArticle();
        // 初始化监听事件
        initListener();
        // 初始化悬浮按钮
        initFloatButton();
    }

    private void initFragment(String articleContent) {
        WrapContentHeightViewPager mViewPager = (WrapContentHeightViewPager) findViewById(R.id.viewpager);
        ArticleDetailAdapter viewPagerAdapter = new ArticleDetailAdapter(getSupportFragmentManager());
        // ————————————————————————————————————————————新建ArticleFragment
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MyApplication.getInstance().getARTICLECONTENT(), articleContent);
        articleFragment.setArguments(bundle);
        //————————————————————————————————————————————新建CommentFragment
        CommentFragment commentFragment = new CommentFragment();
        bundle = new Bundle();
        bundle.putString(MyApplication.getInstance().getARTICLEID(), mArticleContent.getId());
        commentFragment.setArguments(bundle);
        // _________________________________________
        viewPagerAdapter.addFragment(articleFragment);//添加Fragment
        viewPagerAdapter.addFragment(commentFragment);
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器
        // ViewPager切换时NestedScrollView滑动到顶部
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                findViewById(R.id.nestedScrollView).scrollTo(0, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText(MyApplication.getInstance().getArticleTitle()[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(MyApplication.getInstance().getArticleTitle()[1]));
        //给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onNetChange() {
        if (MyApplication.getInstance().getmNetWorkState() != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mArticleContent == null) {
            LoadingArticle();
        }
    }

    private void initListener() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 返回键
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        reHeight = head_layout.getHeight();
        isLessThan = false;
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.color_white));
        // 设置标题
        app_bar_layout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
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
        });
    }

    /**
     * 初始化悬浮按钮并为悬浮按钮设置点击事件
     */
    private void initFloatButton() {
        // 悬浮按钮
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.mipmap.home);
        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView itemIconP = new ImageView(this);
        itemIconP.setImageResource(R.drawable.button_praise_selector);
        SubActionButton praise = itemBuilder.setContentView(itemIconP).build();
        ImageView itemIconC = new ImageView(this);
        itemIconC.setImageResource(R.drawable.button_collect_selector);
        SubActionButton collect = itemBuilder.setContentView(itemIconC).build();

        ImageView itemIconCM = new ImageView(this);
        itemIconCM.setImageResource(R.drawable.button_comment_selector);
        SubActionButton comment = itemBuilder.setContentView(itemIconCM).build();

        final String phoneID = ((MyApplication) getApplication()).getMAC();
        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(praise, 100, 100)
                .addSubActionView(collect, 100, 100)
                .addSubActionView(comment, 100, 100)
                .attachTo(actionButton)
                .build();
        praise.setOnClickListener(v -> {
            new AsyncPraise().execute(mArticleContent.getId());
            actionMenu.close(true);
        });
        collect.setOnClickListener(v -> {
            new AsyncCollect().execute(mArticleContent.getId());
            actionMenu.close(true);
        });
        comment.setOnClickListener(v -> {
            MyApplication.getInstance()
                    .instanceGetComment()
                    .showCommentDialog(ArticleContentActivity.this, String.valueOf(articleId), phoneID);
            actionMenu.close(true);
        });
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
        //       article_markdownView.loadMarkdown(articleContent.getContent());
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


    private class LoadArticle extends AsyncTask<Integer, Integer, ArticleContent> {

        private String mUid = null;

        @Override
        protected void onProgressUpdate(Integer... values) {
            //mProgressBar.setProgress(values[0]);// 每次更新进度条
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
            initAppBarLayout(articleContent);
            // 初始化Fragment
            initFragment(articleContent.getContent());
        }

        /**
         * 判断是否登陆，并且存在uid
         */
        private boolean isLogin() {
            SharedPManager sharedPManager = new SharedPManager(ArticleContentActivity.this);
            if (sharedPManager.isContains(MyApplication.getInstance().getUid())) {
                mUid = sharedPManager.getString(MyApplication.getInstance().getUid(), "0");
                //Log.i("Notzuonotdied", "mUid = " + mUid);
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
