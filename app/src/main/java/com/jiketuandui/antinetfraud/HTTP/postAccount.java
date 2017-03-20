package com.jiketuandui.antinetfraud.HTTP;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.AccountInfo;
import com.jiketuandui.antinetfraud.Bean.CollectionArticle;
import com.jiketuandui.antinetfraud.Bean.HistoryArticle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册登陆
 * Created by Notzuonotdied on 2017/3/7.
 */

public class postAccount extends accessNetwork {

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
     * 登陆
     *
     * @param content 内容
     * @return 是否返回成功
     */
    public AccountInfo postLogin(String content) {
        String json = post("/api/login", content);
        Gson gson = new Gson();
        Type type = new TypeToken<AccountInfo>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 重新登陆
     *
     * @param content 内容
     * @return 是否返回成功
     */
    public boolean postCheckLogin(String content) {
        return TextUtils.equals(post("/api/checklogin", content), "true");
    }

    /**
     * 注册账号
     *
     * @param content 内容  username=123&password=123
     * @return 是否返回成功, 成功返回1，用户名长度不小于5，密码不小于8
     */
    public String postRegister(String content) {
        return post("/api/register", content);

    }

    /**
     * 获取阅读历史
     *
     * @param content 内容
     * @return 是否返回成功, 成功返回1，用户名长度不小于5，密码不小于8
     */
    public boolean postReadHistory(String content) {
        //return TextUtils.equals(post("/api/readArticle", content), "true");
        String mFlag = post("/api/readArticle", content);
        return mFlag != null && Boolean.valueOf(mFlag);
    }

    /**
     * 获取指定的历史记录
     *
     * @param uId 历史记录的ID
     */
    public List<HistoryArticle> getBrowserHistory(String uId) {
        String json = get("/api/getBrowserHistory/" + uId);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<HistoryArticle>>() {

        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将本地的收藏提交到服务器,字段uid，article_id
     */
    public boolean postCollection(String content) {
        //return post("/api/collection", content).equals("true");
        return TextUtils.equals(post("/api/collection", content), "true");
    }

    /**
     * 通过ID
     */
    public List<CollectionArticle> getCollection(String uid) {
        String json = get("/api/getCollection/" + uid);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CollectionArticle>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取消收藏,字段uid，article_id
     */
    public boolean postCancleCollection(String content) {
        //return post("/api/collection", content).equals("true");
        return TextUtils.equals(post("/api/cancelCollection", content), "true");
    }

    /**
     * 意见反馈，content=123
     * */
    public boolean postFeedback(String content) {
        return TextUtils.equals(post("/api/feedback", content),"true");
    }
}
