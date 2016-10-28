package com.jiketuandui.antinetfraud.Bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jiketuandui.antinetfraud.HTTP.getConnect;
import com.jiketuandui.antinetfraud.Util.transTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    private Bitmap mBitmap;

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public String getTip() {
//        return TimeStamp2Date(createtime, "yyyy-MM-dd HH:mm:ss") + " 阅读(" + reading
//                + ")点赞(" + praise + ")";
        return transTime.TimeStamp2Date(createtime, "yyyy-MM-dd") +
                " 阅读(" + reading + ")点赞(" + praise + ")";
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


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        // 增加首行缩进
        return "\u3000\u3000" + content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getTagid() {
        return tagid;
    }

    public String getReading() {
        return reading;
    }

    public String getPraise() {
        return praise;
    }

    public String getImagelink() {

        return imagelink;
    }

    public String getSource() {
        return "来源：" + source;
    }
}
