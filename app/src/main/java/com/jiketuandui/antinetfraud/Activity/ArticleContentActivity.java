package com.jiketuandui.antinetfraud.Activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Activity.Fragment.ArticleDetailFragment.ArticleFragment;
import com.jiketuandui.antinetfraud.Activity.Fragment.ArticleDetailFragment.CommentFragment;
import com.jiketuandui.antinetfraud.Adapter.ArticleDetailAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.WrapContentHeightViewPager;
import com.jiketuandui.antinetfraud.entity.domain.ArticleDetail;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 文章内容显示
 * 文章在点击之后会将浏览记录提交到服务器
 */
public class ArticleContentActivity extends AppCompatActivity
        implements NetBroadcastReceiver.netEventHandler {

    @BindView(R.id.article_title)
    AppCompatTextView articleTitle;
    @BindView(R.id.article_info)
    AppCompatTextView articleInfo;
    @BindView(R.id.article_time)
    AppCompatTextView articleTime;
    @BindView(R.id.head_info)
    LinearLayoutCompat headInfo;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.head_layout)
    SimpleDraweeView headLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ArticleDetail mArticleContent;
    private boolean isCollected;
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;
    /**
     * 这是文章ID
     */
    private int articleId;

    public int dp2px() {
        float scale = getResources().getDisplayMetrics().density;
        return (int) ((float) 66 * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        ButterKnife.bind(this);

        // 根据ID获取文章的内容
        articleId = this.getIntent().getExtras().getInt(MyApplication.getInstance().getCONTENTID());
        Log.i("Notzuonotdied", "articleId = " + articleId);
        // false表示没有被收藏，true表示被收藏
        isCollected = false;
        // 注册
        NetBroadcastReceiver.mListeners.add(this);
        // 读取文章
        loadingArticle();
        // 初始化监听事件
        initListener();
        // 初始化悬浮按钮
        initFloatButton();
    }

    private void initFragment(String articleContent) {
        WrapContentHeightViewPager mViewPager = findViewById(R.id.viewpager);
        ArticleDetailAdapter viewPagerAdapter = new ArticleDetailAdapter(getSupportFragmentManager());
        // ——————————————————————新建ArticleFragment
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MyApplication.getInstance().getARTICLECONTENT(), articleContent);
        articleFragment.setArguments(bundle);
        //——————————————————————新建CommentFragment
        CommentFragment commentFragment = new CommentFragment();
        bundle = new Bundle();
        bundle.putString(MyApplication.getInstance().getARTICLEID(),
                String.valueOf(mArticleContent.getId()));
        commentFragment.setArguments(bundle);
        // _________________________________________
        // 添加Fragment
        viewPagerAdapter.addFragment(articleFragment);
        viewPagerAdapter.addFragment(commentFragment);
        // 设置适配器
        mViewPager.setAdapter(viewPagerAdapter);
        // ViewPager切换时NestedScrollView滑动到顶部
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // findViewById(R.id.nestedScrollView).scrollTo(0, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        //给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText(MyApplication.getInstance().getArticleTitle()[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(MyApplication.getInstance().getArticleTitle()[1]));
        //给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onNetChange() {
        if (MyApplication.getInstance().getNetWorkState() != NetWorkUtils.NET_TYPE_NO_NETWORK &&
                mArticleContent == null) {
            loadingArticle();
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
    }

    /**
     * 初始化悬浮按钮并为悬浮按钮设置点击事件
     */
    private void initFloatButton() {
        // 悬浮按钮
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.mipmap.home);
        final FloatingActionButton actionButton = new FloatingActionButton
                .Builder(this)
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
                .addSubActionView(praise, dp2px(), dp2px())
                .addSubActionView(collect, dp2px(), dp2px())
                .addSubActionView(comment, dp2px(), dp2px())
                .attachTo(actionButton)
                .build();
        praise.setOnClickListener(v -> {
            articleService.praise(articleId)
                    .subscribe(new BaseObserver<>(this, "点赞成功"));
            actionMenu.close(true);
        });
        collect.setOnClickListener(v -> {
            articleService.praise(articleId)
                    .subscribe(new BaseObserver<>(this, "收藏成功"));
            actionMenu.close(true);
        });
//        comment.setOnClickListener(v -> {
//            MyApplication.getInstance()
//                    .instanceGetComment()
//                    .showCommentDialog(ArticleContentActivity.this, String.valueOf(articleId), phoneID);
//            actionMenu.close(true);
//        });
    }

    /**
     * 获取文章内容
     */
    private void loadingArticle() {
        articleService.getArticleDetail(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(dataBeanResult -> {
                    // 将Image URL补全
                    dataBeanResult.getData().setImage(RetrofitServiceFactory.IMAGE_URL +
                            dataBeanResult.getData().getImage());
                    return dataBeanResult;
                })
                .subscribe(new BaseObserver<ArticleDetail>(this) {
                    @Override
                    protected void onHandleSuccess(ArticleDetail articleDetail) {
                        initAppBarLayout(articleDetail);
                        // 初始化Fragment
                        initFragment(articleDetail.getContent());
                    }
                });
        // 更新文章的阅读量
        articleService.updateReading(articleId)
                .subscribe(new BaseObserver<ArticleDetail>(this) {
                    @Override
                    protected void onHandleSuccess(ArticleDetail articleDetail) {

                    }
                });
    }

    /**
     * 设置View一些内容
     */
    private void initAppBarLayout(ArticleDetail articleDetail) {
        mArticleContent = articleDetail;
        articleTitle.setText(mArticleContent.getTitle());
        String info = "阅读:" + articleDetail.getReading() +
                "次,点赞:" + articleDetail.getPraise() + "次";
        articleInfo.setText(info);
        articleTime.setText(mArticleContent.getCreated_at());
        // 设置头图
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(mArticleContent.getImage())
                .setTapToRetryEnabled(true)
                .setOldController(headLayout.getController())
                .build();
        headLayout.setController(controller);
    }

//    private class LoadArticle extends AsyncTask<Integer, Integer, ArticleContent> {
//
//        private String mUid = null;
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            //mProgressBar.setProgress(values[0]);// 每次更新进度条
//        }
//
//        @Override// 在子线程执行
//        protected ArticleContent doInBackground(Integer... integers) {
//            ArticleContent mAContent = ((MyApplication) getApplication()).instanceConnect()
//                    .setArticleURL(integers[0]);
//            // 判断用户是否登陆，假如登陆了，就提交用户的浏览历史
//            if (isLogin() && mAContent != null) {
//                ((MyApplication) getApplication()).instancepostAccount()
//                        .postReadHistory("uid=" + mUid + "&article_id=" + mAContent.getId());
//            }
//            if (mAContent != null) {
//                isCollected = ((MyApplication) getApplication()).isContain(mAContent.getId());
//            }
//            return mAContent;
//        }
//
//        @Override// 在主线程执行
//        protected void onPostExecute(ArticleContent articleContent) {
//            initAppBarLayout(articleContent);
//            // 初始化Fragment
//            initFragment(articleContent.getContent());
//        }

//        /**
//         * 判断是否登陆，并且存在uid
//         */
//        private boolean isLogin() {
//            SharedPManager sharedPManager = new SharedPManager(ArticleContentActivity.this);
//            if (sharedPManager.isContains(MyApplication.getInstance().getUid())) {
//                mUid = sharedPManager.getString(MyApplication.getInstance().getUid(), "0");
//                return true;
//            }
//            return false;
//        }
//    }

//    private class AsyncPraise extends AsyncTask<String, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            return ((MyApplication) getApplication()).instancePraise().setPraiseGet(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            if (aBoolean) {
//                mArticleContent.setPraise(String.valueOf(Integer.valueOf(mArticleContent.getPraise()) + 1));
//                articleInfo.setText(mArticleContent.getInfo());
//                Toast.makeText(ArticleContentActivity.this, "点赞成功~", Toast.LENGTH_SHORT).show();
//            }
//            super.onPostExecute(aBoolean);
//        }
//    }
//
//    private class AsyncCollect extends AsyncTask<String, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            // false表示没有被收藏，true表示被收藏
//            if (isCollected) {// true
//                isCollected = false;
//                return ((MyApplication) getApplication()).instancepostAccount().postCancleCollection(
//                        "uid=" + new SharedPManager(ArticleContentActivity.this)
//                                .getString(MyApplication.getInstance().getUid(), "0")
//                                + "&&article_id=" + strings[0]);
//            } else {// false
//                isCollected = true;
//                return ((MyApplication) getApplication()).instancepostAccount().postCollection(
//                        "uid=" + new SharedPManager(ArticleContentActivity.this)
//                                .getString(MyApplication.getInstance().getUid(), "0")
//                                + "&&article_id=" + strings[0]);
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            if (aBoolean && !isCollected) {
//                Toast.makeText(ArticleContentActivity.this, "取消收藏成功~", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ArticleContentActivity.this, "收藏成功~", Toast.LENGTH_SHORT).show();
//            }
//            // 重置判断缓存
//            ((MyApplication) getApplication()).initCache();
//            super.onPostExecute(aBoolean);
//        }
//    }
}
