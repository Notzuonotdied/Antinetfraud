package com.jiketuandui.antinetfraud.Bean;

import android.graphics.Bitmap;

import com.jiketuandui.antinetfraud.Util.transTime;

/**
 * Created by Notzuonotdied on 2016/7/31.
 * 列表属性
 */
public class ListContent {


    /**
     * id : 1
     * title : 1
     * content : 1
     * createtime : 1468572999
     * tagid : 0
     * reading : 1
     * praise : 0
     * imagelink :图片地址
     * source :来源
     */

    private String id;
    private String title;
    private String content;
    private String createtime;
    private String tagid;
    private String reading;
    private String praise;
    /**
     * 现在imagelink是这样的地址：/application/views/images/9064d4fd25b9197da3081fef.jpg，
     * 需要手动添加http://网址 构成完整路径
     */
    private String imagelink;
    private String source;
    private Bitmap mBitmap;

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getTip() {
        return transTime.TimeStamp2Date(createtime, "yyyy-MM-dd")
               /* + " 阅读(" + reading + ")点赞(" + praise + ")"*/;
    }

    @Override
    public String toString() {
        return "ListContent{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createtime='" + createtime + '\'' +
                ", tagid='" + tagid + '\'' +
                ", reading='" + reading + '\'' +
                ", praise='" + praise + '\'' +
                '}';
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
        // 增加首行缩进
        return "\u3000\u3000" + content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
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
        return "来源：" + source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
