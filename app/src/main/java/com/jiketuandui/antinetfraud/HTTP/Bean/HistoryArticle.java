package com.jiketuandui.antinetfraud.HTTP.Bean;

/**
 * 阅读历史文章的属性
 * Created by Notzuonotdied on 2017/3/15.
 */

public class HistoryArticle {

    /**
     * uid : 4
     * article_id : 3
     * time : 1489470507
     * title : 1212111
     */

    private String uid;
    private String article_id;
    private String time;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public String getArticle_id() {
        return article_id;
    }

    public String getTime() {
        return time;
    }

}
