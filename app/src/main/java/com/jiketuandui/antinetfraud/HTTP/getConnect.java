package com.jiketuandui.antinetfraud.HTTP;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.Bean.TagInfo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Notzuonotdied on 2016/8/1.
 * 从客户端获取数据
 */
public class getConnect extends accessNetwork {
    /**
     * 常量,简介内容链接头部
     */
    public static final String UrlContentHead = "/?/api/article_list/";
    /**
     * 常量,热点的简介内容链接头部
     */
    public static final String UrlContentHot = "/?/api/article_hotlist/";
    private static final String GETCONNECT = "instanceConnect";
    /**
     * 常量,文章内容连接头部
     */
    private static final String UrlArticleHead = "/?/api/view/";
    /**
     * 常量,搜索的简介内容链接头部
     */
    private static final String UrlContentSearch = "/?/api/search/";

    /**
     * 获取内容列表
     * 这里使用的是Gson来处理数据.
     *
     * @param strUrl 地址
     * @return 内容列表
     * @throws IOException the io exception
     */
    @Nullable
    private List<ListContent> getListContents(String strUrl) throws IOException {
        String json = doGet(strUrl);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ListContent>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.i(GETCONNECT, "Access error:getListContents from" + strUrl);
            return null;
        }
    }

    /**
     * 通过Post获取内容列表
     * 这里使用的是Gson来处理数据.
     *
     * @param strUrl 地址
     * @return 内容列表
     * @throws IOException the io exception
     */
    @Nullable
    private List<ListContent> getListContentsBydoPost(String strUrl, String inputString) throws IOException {
        String json = doPost(strUrl, inputString);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ListContent>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.i(GETCONNECT, "Access error:getListContentsBydoPost from" + strUrl +
                    "to search" + inputString);
            return null;
        }
    }

    /**
     * 获取内容列表
     * 这里使用的是Gson来处理数据.
     *
     * @param strUrl 地址
     * @return 内容列表
     * @throws IOException the io exception
     */
    private ArticleContent getArticleContent(String strUrl) throws IOException {
        String json = doGet(strUrl);
        Gson gson = new Gson();
        Type type = new TypeToken<ArticleContent>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.i(GETCONNECT, "Access error:getArticleContent from" + strUrl);
            return null;
        }
    }

    /**
     * 设置链接,Post服务器并返回数据
     *
     * @param readingPage 页数
     * @param inputString 搜索的内容
     * @return List<ListContent>
     */
    @Nullable
    public List<ListContent> setContentPost(String readingPage, String inputString) {
        try {
            return getListContentsBydoPost(UrlContentSearch + readingPage + "/8", inputString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置链接并返回数据
     *
     * @param ArticleId the article id
     * @return ArticleContent对象
     */
    @Nullable
    public ArticleContent setArticleURL(int ArticleId) {
        try {
            return getArticleContent(UrlArticleHead + String.valueOf(ArticleId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置链接并返回数据
     *
     * @param UrlTail the url tail
     * @return List<ListContent>
     */
    @Nullable
    public List<ListContent> setContentURL(String UrlHead, String UrlTail) {
        try {
            return getListContents(UrlHead + UrlTail + "/8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置链接并返回数据
     *
     * @param UrlTail the url tail
     * @param pager   页数
     * @return List<ListContent>
     */
    @Nullable
    public List<ListContent> setContentURL(String UrlHead, String UrlTail, String pager) {
        try {
            return getListContents(UrlHead + UrlTail + "/" + pager);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取内容列表
     * 这里使用的是Gson来处理数据.
     *
     * @param strUrl 地址
     * @return 内容列表
     * @throws IOException the io exception
     */
    @Nullable
    private List<TagInfo> getTagInfo(String strUrl) throws IOException {
        String json = doGet(strUrl);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<TagInfo>>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 设置链接并返回数据
     *
     * @param UrlTail the url tail
     * @return List<ListContent>
     */
    @Nullable
    public List<ListContent> setContentURLByTagId(String UrlHead, String UrlTail,
                                                  String tagId) {
        try {
            return getListContents(UrlHead + UrlTail + "/8/" + tagId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置链接并返回标签数据
     *
     * @return List<TagInfo>
     */
    @Nullable
    public List<TagInfo> setTagInfo() {
        try {
            return getTagInfo("/?/api/get_all_tag");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
