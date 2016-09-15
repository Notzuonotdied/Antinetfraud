package com.jiketuandui.antinetfraud.HTTP;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.Bean.TagInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Notzuonotdied on 2016/8/1.
 * 从客户端获取数据
 */
public class getConnect {
    private static final String GETCONNECT = "getConnect";
    //public static String myUrl = "http://127.0.0.1";
    //public static String myUrl = "http://10.0.3.2";
    //public static String myUrl = "http://192.168.0.149";
    public static String myUrl = "http://192.168.0.104";

    //public static String myUrl = "http://mlen.ittun.com";
    /**
     * 常量,简介内容链接头部
     */
    public static String UrlContentHead = myUrl + "/?/api/article_list/";
    /**
     * 常量,热点的简介内容链接头部
     */
    public static String UrlContentHot = myUrl + "/?/api/article_hotlist/";
    /**
     * 常量,文章内容连接头部
     */
    private static String UrlArticleHead = myUrl + "/?/api/view/";
    /**
     * 常量,搜索的简介内容链接头部
     */
    private static String UrlContentSearch = myUrl + "/?/api/search/";

    /**
     * 获取Json数组
     *
     * @param mURL 地址
     * @return Json数组
     * @throws IOException the io exception
     */
    @Nullable
    private static String doGet(String mURL) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            /**
             * 使用HttpURLConnection的步骤是先实例化一个URL对象，通过URL的openConnection实例化
             * HttpURLConnection对象。然后设置参数，注意此时并没有发生连接。真正发生连接是在获得流
             * 时即conn.getInputStream这一句时，这点跟TCP Socket是一样的。并非阻塞在
             * ServerSocket.accept()而是阻塞在获取流。所以在获取流之前应该设置好所有的参数。
             * */
            url = new URL(mURL);
            /**
             * 此处的urlConnection对象实际上是根据URL的
             * 请求协议(此处是http)生成的URLConnection类
             * 的子类HttpURLConnection,故此处最好将其转化
             * 为HttpURLConnection类型的对象,以便用到
             * HttpURLConnection更多的API.如下:
             */
            urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");

            // 数据流
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        if (stringBuffer == null) {
            return null;
        }
        return stringBuffer.toString();
    }

    /**
     * 将用户搜索的数据提交到服务器并获取服务器返回的数据
     *
     * @param mURL        地址
     * @param inputString 搜索的内容
     */
    private static String doPost(String mURL, String inputString) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            /**
             * 使用HttpURLConnection的步骤是先实例化一个URL对象，通过URL的openConnection实例化
             * HttpURLConnection对象。然后设置参数，注意此时并没有发生连接。真正发生连接是在获得流
             * 时即conn.getInputStream这一句时，这点跟TCP Socket是一样的。并非阻塞在
             * ServerSocket.accept()而是阻塞在获取流。所以在获取流之前应该设置好所有的参数。
             * */
            url = new URL(mURL);
            /**
             * 此处的urlConnection对象实际上是根据URL的
             * 请求协议(此处是http)生成的URLConnection类
             * 的子类HttpURLConnection,故此处最好将其转化
             * 为HttpURLConnection类型的对象,以便用到
             * HttpURLConnection更多的API.如下:
             */
            urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            httpURLConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设定请求的方法为"POST"，默认是GET
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("accept", "*/*");
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.connect();
/*
            OutputStream outStrm = httpURLConnection.getOutputStream();
            // 现在通过输出流对象构建对象输出流对象,以实现输出可序列化的对象
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStrm);
            // 向对象输出流写出数据,将这些数据存数到内存缓冲区
            objectOutputStream.writeBytes("value="+URLEncoder.encode(inputString,"UTF-8"));
            // 刷新对象输出流,将任何字节都写入到潜在的流中(此处为ObjectOutputStream)
            objectOutputStream.flush();
            // 关闭流对象,此时,不能继续向对象输出流写入任何数据,先前写入的数据存在于内存缓冲区中,
            // 在调用下边的getInputStream()函数的时候才把准备好的http请求正式发送到服务器
            objectOutputStream.close();
*/
            PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
            inputString = new String(inputString.getBytes(), "UTF-8");
            out.print("value=" + inputString);
            out.close();

            // 数据流
            // 调用HttpURLConnection连接对象的getInputStream()函数,
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            inputStream = httpURLConnection.getInputStream();//<===注意，实际发送请求的代码段就在这里
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        if (stringBuffer == null) {
            return null;
        }
        return stringBuffer.toString();
    }

    /**
     * 获取内容列表
     * 这里使用的是Gson来处理数据.
     *
     * @param strUrl 地址
     * @return 内容列表
     * @throws IOException the io exception
     */
    private static List<ListContent> getListContents(String strUrl) throws IOException {
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
    private static List<ListContent> getListContentsBydoPost(String strUrl, String inputString) throws IOException {
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
    private static ArticleContent getArticleContent(String strUrl) throws IOException {
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
    public static List<ListContent> setContentPost(String readingPage, String inputString) {
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
    public static ArticleContent setArticleURL(int ArticleId) {
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
    public static List<ListContent> setContentURL(String UrlHead, String UrlTail) {
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
     * @param pager 页数
     * @return List<ListContent>
     */
    @Nullable
    public static List<ListContent> setContentURL(String UrlHead, String UrlTail, String pager) {
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
    private static List<TagInfo> getTagInfo(String strUrl) throws IOException {
        String json = getConnect.doGet(strUrl);
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
    public static List<ListContent> setContentURLByTagId(String UrlHead, String UrlTail,
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
    public static List<TagInfo> setTagInfo() {
        try {
            return getTagInfo(myUrl + "/?/api/get_all_tag");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
