package com.jiketuandui.antinetfraud.HTTP;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.CommentInfo;

import java.lang.reflect.Type;

/**
 * 评论类
 * Created by wangyu on 17-3-22.
 */
public class getComment extends accessNetwork {
    private String post(String url, String content) {
        String str = null;
        try {
            str = doPost(url, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String get(String url) {
        String str = null;
        try {
            str = doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取文章的评论数组,username="123"&token="123"
     */
    public CommentInfo getComment(String articleId) {
        String json = get("/api/getComment/" + articleId);
        Gson gson = new Gson();
        Type type = new TypeToken<CommentInfo>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * psot评论,username="123"&token="123"&phone_id="123"&article_id="17"&user_id="12"&content="123"
     */
    private boolean postComment(String content) {
        return TextUtils.equals(post("api/comment/", content), "true");
    }
}
