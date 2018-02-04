package com.jiketuandui.antinetfraud.activity.article;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.enums.Animation;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.Adapter.ArticleDetailAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.Constants;
import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;
import com.jiketuandui.antinetfraud.View.WrapContentHeightViewPager;
import com.jiketuandui.antinetfraud.activity.article.detail.ArticleFragment;
import com.jiketuandui.antinetfraud.activity.article.detail.CommentFragment;
import com.jiketuandui.antinetfraud.entity.domain.ArticleDetail;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.retrofirt.RetrofitServiceFactory;
import com.jiketuandui.antinetfraud.retrofirt.rxjava.BaseObserver;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;
import com.jiketuandui.antinetfraud.retrofirt.service.UserService;
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
 *
 * @author wangyu
 */
public class ArticleContentActivity extends AppCompatActivity
        implements NetBroadcastReceiver.NetEventHandler {

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
    private ArticleService articleService = RetrofitServiceFactory.ARTICLE_SERVICE;
    private UserService userService = RetrofitServiceFactory.USER_SERVICE;
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
        articleId = this.getIntent().getExtras().getInt(Constants.CONTENT_ID);
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
        bundle.putString(Constants.ARTICLE_CONTENT, articleContent);
        articleFragment.setArguments(bundle);
        //——————————————————————新建CommentFragment
        CommentFragment commentFragment = new CommentFragment();
        bundle = new Bundle();
        bundle.putInt(Constants.ARTICLE_ID, mArticleContent.getId());
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
        // 给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.ARTICLE_TITLE[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.ARTICLE_TITLE[1]));
        // 给TabLayout设置关联ViewPager，如果设置了ViewPager，
        // 那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onNetChange() {
        if (MyApplication.NetWorkState != NetWorkUtils.NET_TYPE_NO_NETWORK &&
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

        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(praise, dp2px(), dp2px())
                .addSubActionView(collect, dp2px(), dp2px())
                .addSubActionView(comment, dp2px(), dp2px())
                .attachTo(actionButton)
                .build();
        praise.setOnClickListener(v -> {
            articleService.praise(articleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<>(this, "点赞成功"));
            actionMenu.close(true);
        });
        collect.setOnClickListener(v -> {
            User user = MyApplication.getInstance().getUser();
            if (MyApplication.getInstance().getUser() != null) {
                ToastUtils.showShort("请登录～");
                return;
            }
            long millis = TimeUtils.date2Millis(TimeUtils.getNowDate());
            userService.collect(user.getId(),
                    MyApplication.getInstance().getSign(millis, "auth/article/collection"),
                    millis, articleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ArticleList>(this, "收藏成功") {
                        @Override
                        protected void onHandleFailure(String message) {
                            ToastUtils.showShort(message);
                        }
                    });
            actionMenu.close(true);
        });
        comment.setOnClickListener(v -> {
            new PanterDialog(this)
                    .setHeaderBackground(R.mipmap.pattern_bg_blue)
                    .setTitle("网络诈骗防范科普网", 20)
                    .setPositive(getString(R.string.confirm))
                    .withAnimation(Animation.SIDE)
                    .setDialogType(DialogType.INPUT)
                    .isCancelable(false)
                    .input("请输入您的评论内容", this::postComment)
                    .show();
            actionMenu.close(true);
        });
    }

    private void postComment(String content) {
        if (MyApplication.getInstance().getUser() != null) {
            ToastUtils.showShort("请登录～");
            return;
        }
        long millis = MyApplication.getInstance().getMillis();
        userService.comment(
                MyApplication.getInstance().getUser().getId(),
                MyApplication.getInstance().getSign(millis, "auth/article/comment"),
                millis, articleId, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<User>(getApplicationContext(), "评论成功") {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showShort("评论失败");
                    }
                });
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
        // 保存用户浏览历史
        long millis = MyApplication.getInstance().getMillis();
        if (MyApplication.getInstance().getUser() != null) {
            userService.saveHistory(MyApplication.getInstance().getUser().getId(),
                    MyApplication.getInstance().getSign(millis, "auth/article/read"),
                    millis, articleId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<>());
        }
        // 更新文章的阅读量
        articleService.updateReading(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<>(this));
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
}
