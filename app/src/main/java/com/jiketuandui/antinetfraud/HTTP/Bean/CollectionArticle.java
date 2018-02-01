package com.jiketuandui.antinetfraud.HTTP.Bean;

/**
 * Created by Notzuonotdied on 2017/3/15.
 */

public class CollectionArticle {

    /**
     * id : 2
     * uid : 4
     * article_id : 6
     * title : 1111111
     */

    private String id;
    private String uid;
    private String article_id;
    private String title;

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getArticle_id() {
        return article_id;
    }

    public String getTitle() {
        return title;
    }
}
