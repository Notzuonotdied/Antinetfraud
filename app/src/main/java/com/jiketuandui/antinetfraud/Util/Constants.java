package com.jiketuandui.antinetfraud.Util;

/**
 * @author wangyu
 * @data 18-2-1
 * @describe 常量类
 */

public class Constants {
    public final static String USER_DATA = "userData";
    /**
     * 用户注册时候返回的Token
     */
    public static final String TOKEN;
    /**
     * 用户的名称
     */
    public static final String USERNAME;
    /**
     * 用户注册的ID
     */
    public static final String UID;
    /**
     * 当前ViewPageIndication的子项的position
     */
    public static final String MAIN_PAGE_POSITION;
    public static final String MAIN_PAGE_POSITION_HOT;
    public static final String SEARCH_STRING;
    public static final String TAGS_ID;
    /**
     * 当前的Content的id
     */
    public static final String CONTENT_ID;
    /**
     * 当前的Content的id
     */
    public static final String ANNOUNCE_ID;
    /**
     * 当前文章的ID
     */
    public static final String ARTICLE_ID;
    /**
     * 当前的文章的Markdown格式内容
     */
    public static final String ARTICLE_CONTENT;
    public static final String[] TAB_TITLE;
    public static final String[] TAB_BIG_TITLE;
    public static final String[] TAB_BIG_TITLE_HOT;
    public static final String[] HEADER_TEXT_VIEW;
    public static final String[] ARTICLE_TITLE;
    public static final String DATA_FORMAT;

    static {
        TOKEN = "TOKEN";
        USERNAME = "USERNAME";
        UID = "UID";
        MAIN_PAGE_POSITION = "MainPagePosition";
        MAIN_PAGE_POSITION_HOT = "MainPagePositionHot";
        SEARCH_STRING = "SearchString";
        TAGS_ID = "TagsId";
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
        DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }
}
