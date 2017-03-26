package com.jiketuandui.antinetfraud.Util;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jiketuandui.antinetfraud.Adapter.AnnounceAdapter;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.Bean.CollectionArticle;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.HTTP.getAnnouncement;
import com.jiketuandui.antinetfraud.HTTP.getCommentInfo;
import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.HTTP.getPraise;
import com.jiketuandui.antinetfraud.HTTP.postAccount;
import com.jiketuandui.antinetfraud.HTTP.postShareContent;

import java.util.List;

/**
 * Created by Notzuonotdied on 2016/8/6.
 * 全局变量类
 */
public class MyApplication extends Application {
    /**
     * 单例模式
     */
    private static MyApplication myApplication;
    /**
     * 用户注册时候返回的Token
     */
    private String mToken;
    /**
     * 用户的名称
     */
    private String username;
    /**
     * 用户注册的ID
     */
    private String uid;
    /**
     * 当前ViewPgaeIndication的子项的position
     */
    private String MAINPAGEPOSITON;
    private String MAINPAGEPOSITONHOT;
    private String SEARCHSTRING;
    private String TAGSID;
    private String CATEGORY;
    /**
     * 当前的Content的id
     */
    private String CONTENTID;
    /**
     * 当前的Content的id
     */
    private String ANNOUNCEID;
    /**
     * 当前文章的ID
     */
    private String ARTICLEID;
    /**
     * 当前的文章的Markdown格式内容
     */
    private String ARTICLECONTENT;
    /**
     * 当前网络状态
     */
    private int mNetWorkState;
    private String[] TabTitle;
    private String[] TabBigTitle;
    private String[] TabBigTitleHot;
    private String[] HeaderTextView;
    private String[] ArticleTitle;
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
    public MyApplication() {
        /*初始化变量*/
        initField();
    }

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
        return ARTICLEID;
    }

    public String getARTICLECONTENT() {
        return ARTICLECONTENT;
    }

    public String[] getArticleTitle() {
        return ArticleTitle;
    }

    public boolean getLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getmToken() {
        return mToken;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getMAINPAGEPOSITON() {
        return MAINPAGEPOSITON;
    }

    public String getMAINPAGEPOSITONHOT() {
        return MAINPAGEPOSITONHOT;
    }

    public String getSEARCHSTRING() {
        return SEARCHSTRING;
    }

    public String getTAGSID() {
        return TAGSID;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public String getCONTENTID() {
        return CONTENTID;
    }

    public String getANNOUNCEID() {
        return ANNOUNCEID;
    }

    public int getmNetWorkState() {
        return mNetWorkState;
    }

    public void setmNetWorkState(int mNetWorkState) {
        this.mNetWorkState = mNetWorkState;
    }

    public String[] getTabTitle() {
        return TabTitle;
    }

    public String[] getTabBigTitle() {
        return TabBigTitle;
    }

    public String[] getTabBigTitleHot() {
        return TabBigTitleHot;
    }

    public String[] getHeaderTextView() {
        return HeaderTextView;
    }

    /**
     * 判断是否重复
     */
    public boolean isContainLists(ListContentAdapter mListContentAdapter,
                                  List<ListContent> ListContents) {
        for (ListContent ml : ListContents) {
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
    public boolean isContainLists(AnnounceAdapter mListContentAdapter,
                                  List<AnnounceContent> ListContents) {
        for (AnnounceContent ml : ListContents) {
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
        /*初始化Tag标签列表*/
        initTagsList();
        /*初始化缓存数据*/
        initCache();
        /*初始化登陆状态*/
        initLoginState();
        super.onCreate();
    }

    /**
     * 初始化Field
     */
    private void initField() {
        mToken = "TOKEN";
        username = "USERNAME";
        uid = "UID";
        MAINPAGEPOSITON = "MainPagePosition";
        MAINPAGEPOSITONHOT = "MainPagePositionHot";
        SEARCHSTRING = "SearchString";
        TAGSID = "TagsId";
        CATEGORY = "Category";
        CONTENTID = "ContentId";
        ANNOUNCEID = "AnnounceId";
        ARTICLECONTENT = "ArticleContent";
        ARTICLEID = "ArticleId";
        TabTitle = new String[]{
                "最新", "奖励", "诱惑", "信息", "通知", "传销", "投资",
                "短信", "通讯", "工具", "病毒", "充值", "交易", "钓鱼", "伪造", "其他"
        };
        TabBigTitle = new String[]{
                "最新消息", "网络诈骗", "电信诈骗", "小小贴士"
        };
        TabBigTitleHot = new String[]{
                "总排行榜", "电信诈骗", "网络诈骗", "小小贴士"
        };
        HeaderTextView = new String[]{
                "网络诈骗防范科普网", "热门案例排行榜",
                "案例搜索", "个人设置"
        };
        ArticleTitle = new String[]{
                "文章详情", "评论列表"
        };
    }

    private void initLoginState() {
        SharedPManager sp = new SharedPManager(MyApplication.this);
        isLogin = sp.getString(mToken, null) != null;
    }

    /**
     * 初始化Service进行监听网络
     */
    public void initNETService() {
//        Intent i = new Intent(this, NetworkStateService.class);
//        startService(i);
        mNetWorkState = NetWorkUtils.getNetWorkState(MyApplication.this);
    }

    private void initTagsList() {

    }

    /**
     * 获取屏幕的参数，宽度和高度
     */
    private void initScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)
                this.getSystemService(Context.WINDOW_SERVICE);
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
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
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
    public boolean isContain(String ID) {
        for (CollectionArticle c : collectionArticles) {
            if (c.getArticle_id().equals(ID)) {
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
                            .getString(uid, null));
//            historyArticles = instancepostAccount().getBrowserHistory(
//                    new SharedPManager(MyApplication.this).getString(MyApplication.uid, null));
            return collectionArticles != null /*&& historyArticles != null*/;
        }
    }
}
