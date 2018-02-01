package com.jiketuandui.antinetfraud.Util;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jiketuandui.antinetfraud.Adapter.AnnounceAdapter;
import com.jiketuandui.antinetfraud.Adapter.CommentAdapter;
import com.jiketuandui.antinetfraud.HTTP.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.HTTP.Bean.CollectionArticle;
import com.jiketuandui.antinetfraud.HTTP.Bean.CommentInfo;
import com.jiketuandui.antinetfraud.HTTP.getAnnouncement;
import com.jiketuandui.antinetfraud.HTTP.getCommentInfo;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.HTTP.getPraise;
import com.jiketuandui.antinetfraud.HTTP.postAccount;
import com.jiketuandui.antinetfraud.HTTP.postShareContent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Notzuonotdied on 2016/8/6.
 * 全局变量类
 */
public class MyApplication extends Application {

    /**
     * 用户注册时候返回的Token
     */
    private static final String TOKEN;
    /**
     * 用户的名称
     */
    private static final String USERNAME;
    /**
     * 用户注册的ID
     */
    private static final String UID;
    /**
     * 当前ViewPageIndication的子项的position
     */
    private static final String MAIN_PAGE_POSITION;
    private static final String MAIN_PAGE_POSITION_HOT;
    private static final String SEARCH_STRING;
    private static final String TAGS_ID;
    private static final String CATEGORY;
    /**
     * 当前的Content的id
     */
    private static final String CONTENT_ID;
    /**
     * 当前的Content的id
     */
    private static final String ANNOUNCE_ID;
    /**
     * 当前文章的ID
     */
    private static final String ARTICLE_ID;
    /**
     * 当前的文章的Markdown格式内容
     */
    private static final String ARTICLE_CONTENT;
    private static final String[] TAB_TITLE;
    private static final String[] TAB_BIG_TITLE;
    private static final String[] TAB_BIG_TITLE_HOT;
    private static final String[] HEADER_TEXT_VIEW;
    private static final String[] ARTICLE_TITLE;
    private static MyApplication myApplication;

    // 初始化Field
    static {
        TOKEN = "TOKEN";
        USERNAME = "USERNAME";
        UID = "UID";
        MAIN_PAGE_POSITION = "MainPagePosition";
        MAIN_PAGE_POSITION_HOT = "MainPagePositionHot";
        SEARCH_STRING = "SearchString";
        TAGS_ID = "TagsId";
        CATEGORY = "Category";
        CONTENT_ID = "ContentId";
        ANNOUNCE_ID = "AnnounceId";
        ARTICLE_CONTENT = "ArticleContent";
        ARTICLE_ID = "ArticleId";
        TAB_TITLE = new String[]{
                "最新", "奖励", "诱惑", "信息", "通知", "传销", "投资",
                "短信", "通讯", "工具", "病毒", "充值", "交易", "钓鱼", "伪造", "其他"
        };
        TAB_BIG_TITLE = new String[]{
                "最新消息", "网络诈骗", "电信诈骗", "小小贴士"
        };
        TAB_BIG_TITLE_HOT = new String[]{
                "总排行榜", "电信诈骗", "网络诈骗", "小小贴士"
        };
        HEADER_TEXT_VIEW = new String[]{
                "网络诈骗防范科普网", "热门案例排行榜",
                "案例搜索", "个人设置"
        };
        ARTICLE_TITLE = new String[]{
                "文章详情", "评论列表"
        };
    }

    /**
     * 当前网络状态
     */
    private int mNetWorkState;
    /**
     * 判断是否登录了
     */
    private boolean isLogin;
    /**
     * 屏幕的宽度
     */
    private int myScreenWidth;
    /**
     * 屏幕的高度
     */
    private int myScreenHeight;
    /**
     * 用于判断是否被收藏了
     */
    private List<CollectionArticle> collectionArticles;

    /**
     * 单例模式
     */
    public static MyApplication getInstance() {
        if (myApplication == null) {
            synchronized (MyApplication.class) {
                if (myApplication == null) {
                    myApplication = new MyApplication();
                }
            }
        }
        return myApplication;
    }

    public String getARTICLEID() {
        return ARTICLE_ID;
    }

    public String getARTICLECONTENT() {
        return ARTICLE_CONTENT;
    }

    public String[] getArticleTitle() {
        return ARTICLE_TITLE;
    }

    public boolean getLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getToken() {
        return TOKEN;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getUid() {
        return UID;
    }

    public String getMAINPAGEPOSITON() {
        return MAIN_PAGE_POSITION;
    }

    public String getMAINPAGEPOSITONHOT() {
        return MAIN_PAGE_POSITION_HOT;
    }

    public String getSEARCHSTRING() {
        return SEARCH_STRING;
    }

    public String getTAGSID() {
        return TAGS_ID;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public String getCONTENTID() {
        return CONTENT_ID;
    }

    public String getANNOUNCEID() {
        return ANNOUNCE_ID;
    }

    public int getNetWorkState() {
        return mNetWorkState;
    }

    public void setNetWorkState(int mNetWorkState) {
        this.mNetWorkState = mNetWorkState;
    }

    public String[] getTabTitle() {
        return TAB_TITLE;
    }

    public String[] getTabBigTitle() {
        return TAB_BIG_TITLE;
    }

    public String[] getTabBigTitleHot() {
        return TAB_BIG_TITLE_HOT;
    }

    public String[] getHeaderTextView() {
        return HEADER_TEXT_VIEW;
    }

//    /**
//     * 判断是否重复
//     */
//    public boolean isContainLists(ListContentAdapter mListContentAdapter,
//                                  List<ListContent> listContents) {
//        for (ListContent ml : listContents) {
//            for (int i = 0; i < mListContentAdapter.getData().size(); i++) {
//                if (mListContentAdapter.getData().get(i).getId().equals(ml.getId())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 判断是否重复
     */
    public boolean isContainLists(AnnounceAdapter mListContentAdapter,
                                  List<AnnounceContent> listContents) {
        for (AnnounceContent ml : listContents) {
            for (int i = 0; i < mListContentAdapter.getData().size(); i++) {
                if (mListContentAdapter.getData().get(i).getId().equals(ml.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否重复
     */
    public boolean isContainComment(CommentAdapter mListContentAdapter,
                                    List<CommentInfo> listContents) {
        for (CommentInfo ml : listContents) {
            for (int i = 0; i < mListContentAdapter.getData().size(); i++) {
                if (mListContentAdapter.getData().get(i).getId().equals(ml.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        /*初始化Fresco类*/
        Fresco.initialize(this);
        /*初始化网络监听机制*/
        initNETService();
        /*初始化获取屏幕的宽度和高度*/
        initScreenWidth();
        /*初始化缓存数据*/
        initCache();
        super.onCreate();
    }

    public void initLoginState() {
        SharedPManager sp = new SharedPManager(MyApplication.this);
        isLogin = sp.getString(TOKEN, null) != null;
    }

    /**
     * 初始化Service进行监听网络
     */
    public void initNETService() {
        mNetWorkState = NetWorkUtils.getNetWorkState(MyApplication.this);
    }

    /**
     * 获取屏幕的参数，宽度和高度
     */
    private void initScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)
                this.getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        windowManager.getDefaultDisplay().getMetrics(metrics);
        myScreenHeight = metrics.heightPixels;
        myScreenWidth = metrics.widthPixels;
    }


    public int getMyScreenWidth() {
        return myScreenWidth;
    }

    public void setMyScreenWidth(int myScreenWidth) {
        this.myScreenWidth = myScreenWidth;
    }

    public int getMyScreenHeight() {
        return myScreenHeight;
    }

    public void setMyScreenHeight(int myScreenHeight) {
        this.myScreenHeight = myScreenHeight;
    }

    /**
     * 分享经历
     */
    public postShareContent getPostShareContent() {
        return new postShareContent();
    }

    /**
     * 分享经历
     */
    public getConnect instanceConnect() {
        return new getConnect();
    }

    /**
     * 点赞
     */
    public getPraise instancePraise() {
        return new getPraise();
    }

    /**
     * 评论
     */
    public getCommentInfo instanceGetComment() {
        return new getCommentInfo();
    }

    /**
     * 公告
     */
    public getAnnouncement instanceAnnouncement() {
        return new getAnnouncement();
    }

    /**
     * 登陆
     */
    public postAccount instancepostAccount() {
        return new postAccount();
    }

    /**
     * MAC
     */
    public String getMAC() {
        return getUniquePseudoID();
    }

    public String getUniquePseudoID() {
        String serial;

        String mSzdevidshort = "233" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 + Build.USER.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10; //12 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(mSzdevidshort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // serial需要一个初始化
            // 随便一个初始化
            serial = "serial";
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(mSzdevidshort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 缓存数据
     */
    public void initCache() {
        new AsyncData().execute();
    }

    /**
     * 检查文章ID是否在
     */
    public boolean isContain(String id) {
        for (CollectionArticle c : collectionArticles) {
            if (c.getArticle_id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private class AsyncData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            collectionArticles = instancepostAccount().getCollection(
                    new SharedPManager(MyApplication.this)
                            .getString(UID, null));
//            historyArticles = instancepostAccount().getBrowserHistory(
//                    new SharedPManager(MyApplication.this).getString(MyApplication.UID, null));
            return collectionArticles != null /*&& historyArticles != null*/;
        }
    }
}
