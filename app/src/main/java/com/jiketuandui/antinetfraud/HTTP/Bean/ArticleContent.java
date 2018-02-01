package com.jiketuandui.antinetfraud.HTTP.Bean;

import com.jiketuandui.antinetfraud.Util.transTime;

import java.util.List;

/**
 * Created by Notzuonotdied on 2016/7/31.
 * 文章内容显示
 */
public class ArticleContent {

    /**
     * id : 1
     * title : 1
     * content : 1
     * createtime : 1468572999----createtime为unix时间戳
     * tagid : 0
     * reading : 7
     * praise : 0
     * imagelink :
     * source :
     * tags : [null]----tag为标签名.
     */

    private String id;
    private String title;
    private String content;
    private String createtime;
    private String tagid;
    private String reading;
    private String praise;
    private String imagelink;
    private String source;
    private List<TagInfo> tags;

    public String getInfo() {
        return "阅读:" + getReading() + "次,点赞:" + getPraise() + "次";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return transTime.getTime(createtime);
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<TagInfo> getTags() {
        return tags;
    }

    public void setTags(List<TagInfo> tags) {
        this.tags = tags;
    }
}
