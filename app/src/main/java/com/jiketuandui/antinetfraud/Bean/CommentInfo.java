package com.jiketuandui.antinetfraud.Bean;

/**
 * 评论
 * Created by wangyu on 17-3-22.
 */

public class CommentInfo {
    /**
     * id : 3
     * article_id : 17
     * content : 1222
     * state : 1
     * author : Edward
     * create_at : 0
     */

    private String id;
    private String article_id;
    private String content;
    private String state;
    private String author;
    private String create_at;

    public void setId(String id) {
        this.id = id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getId() {
        return id;
    }

    public String getArticle_id() {
        return article_id;
    }

    public String getContent() {
        return content;
    }

    public String getState() {
        return state;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreate_at() {
        return create_at;
    }
}
