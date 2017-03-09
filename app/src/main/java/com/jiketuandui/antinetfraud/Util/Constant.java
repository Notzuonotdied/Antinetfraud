package com.jiketuandui.antinetfraud.Util;

import com.jiketuandui.antinetfraud.Adapter.AnnounceAdapter;
import com.jiketuandui.antinetfraud.Adapter.ListContentAdapter;
import com.jiketuandui.antinetfraud.Bean.AnnounceContent;
import com.jiketuandui.antinetfraud.Bean.ListContent;

import java.util.List;

/**
 * Created by Notzuonotdied on 2016/7/31.
 * 常量类
 */
public class Constant {

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

    public static String[] TabTitle = {"最新", "奖励", "诱惑", "信息", "通知", "传销", "投资",
            "短信", "通讯", "工具", "病毒", "充值", "交易", "钓鱼", "伪造", "其他"};

    public static String[] TabBigTitle = {"最新消息", "网络诈骗", "电信诈骗", "小小贴士"};
    public static String[] TabBigTitle_hot = {"总排行榜", "电信诈骗", "网络诈骗", "小小贴士"};
    public static String[] Header_TextView = {"网络诈骗防范科普网", "热门案例排行榜",
            "案例搜索", "个人设置"};

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

}
