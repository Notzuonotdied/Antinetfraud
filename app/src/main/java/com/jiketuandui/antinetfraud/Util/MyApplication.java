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
     * 用户注册时候返回的Token
     */
    public static final String mToken = "TOKEN";
    /**
     * 用户的名称
     */
    public static final String username = "USERNAME";
    /**
     * 用户注册的ID
     */
    public static final String uid = "UID";
    /**
     * 当前ViewPgaeIndication的子项的position
     */
    public static final String MAINPAGEPOSITON = "mainpageposition";
    public static final String MAINPAGEPOSITONHOT = "mainpagepositionhot";
    public static final String SEARCHSTRING = "searchstring";
    public static final String TAGSID = "tagsid";
    public static final String CATEGORY = "category";
    /**
     * 当前的Content的id
     */
    public static final String CONTENTID = "ContentId";
    /**
     * 当前的Content的id
     */
    public static final String ANNOUNCEID = "AnnounceId";
    /**
     * 当前的历史记录的ID
     */
    public static final String HISTORY = "History";
    public static int mNetWorkState;
    public static String[] TabTitle = {"最新", "奖励", "诱惑", "信息", "通知", "传销", "投资",
            "短信", "通讯", "工具", "病毒", "充值", "交易", "钓鱼", "伪造", "其他"};
    public static String[] TabBigTitle = {"最新消息", "网络诈骗", "电信诈骗", "小小贴士"};
    public static String[] TabBigTitle_hot = {"总排行榜", "电信诈骗", "网络诈骗", "小小贴士"};
    public static String[] Header_TextView = {"网络诈骗防范科普网", "热门案例排行榜",
            "案例搜索", "个人设置"};
    public static boolean isLogin;
    private static MyApplication myApplication;
    private int myScreenWidth;
    private int myScreenHeight;
    //    private List<HistoryArticle> historyArticles;
    private List<CollectionArticle> collectionArticles;

    public static MyApplication getInstance() {
        return myApplication;
    }

    /**
     * 判断是否重复
     */
    public static boolean isContainLists(ListContentAdapter mListContentAdapter,
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
    public static boolean isContainLists(AnnounceAdapter mListContentAdapter,
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
        super.onCreate();
        /*初始化Fresco类*/
        Fresco.initialize(this);
        /*初始化获取屏幕的宽度和高度*/
        initScreenWidth();
        /*初始化Tag标签列表*/
        initTagsList();
        /*初始化网络监听机制*/
        initNETService();
        /*初始化缓存数据*/
        initCache();
        /*初始化登陆状态*/
        initLoginState();
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
                            .getString(MyApplication.uid, null));
//            historyArticles = instancepostAccount().getBrowserHistory(
//                    new SharedPManager(MyApplication.this).getString(MyApplication.uid, null));
            return collectionArticles != null /*&& historyArticles != null*/;
        }
    }
}
