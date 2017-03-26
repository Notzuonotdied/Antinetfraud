package com.jiketuandui.antinetfraud.HTTP;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.AnnounceContent;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取公告
 * Created by Notzuonotdied on 2017/3/8.
 */

public class getAnnouncement extends accessNetwork {
    private static final String GETCONNECT = "instanceConnect";
    /**
     * 常量,获取公告列表
     */
    private static final String UrlAnnounceList = "/api/noticelist";
    /**
     * 常量,获取公告列表
     */
    private static final String UrlAnnounceCon = "/api/noticeview/";

    /**
     * 获取内容列表
     * 这里使用的是Gson来处理数据.
     *
     * @return 内容列表
     */
    @Nullable
    public List<AnnounceContent> getAnnounceList() {
        String json = null;
        try {
            json = doGet("/api/noticelist");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AnnounceContent>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.i(GETCONNECT, "Access error:getAnnounceList from" + "/api/noticelist");
            return null;
        }
    }

    /**
     * 获取内容列表
     * 这里使用的是Gson来处理数据.
     *
     * @param id id
     * @return 内容列表
     */
    @Nullable
    public AnnounceContent getAnnounce(String id) {
        String json = null;
        try {
            json = doGet("/api/noticeview/" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<AnnounceContent>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.i(GETCONNECT, "Access error:getAnnounceList from" + "/api/noticeview/" + id);
            return null;
        }
    }
}
