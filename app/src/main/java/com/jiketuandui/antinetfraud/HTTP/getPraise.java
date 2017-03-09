package com.jiketuandui.antinetfraud.HTTP;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 点赞
 * Created by Notzuonotdied on 2017/3/7.
 */

public class getPraise extends accessNetwork {

    /**
     * 常量,点赞的链接头部
     */
    private static final String UrlPraise = "/?/api/praise/";

    /**
     * 将点赞提交到服务器并获取服务器返回的结果
     *
     * @param mURL      地址
     * @param articleId 点赞文章Id
     */
    @NonNull
    private Boolean doPraisePost(String mURL, String articleId) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String line = null;
        try {
            url = new URL(myUrl + mURL);
            urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.connect();

            PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
            articleId = new String(articleId.getBytes(), "UTF-8");
            out.print("id=" + articleId);
            out.close();
            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            reader = new BufferedReader(inputStreamReader);
            while ((line = reader.readLine()) != null) {
                line += line;
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
        }
        return line != null && line.equals("true");
    }

    /**
     * 点赞并获取返回结果
     *
     * @param mURL 地址
     * @return boolean 返回结果-是否成功
     * @throws IOException the io exception
     */
    private boolean doPraiseGet(String mURL) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            url = new URL(myUrl + mURL);
            urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod("GET");
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
        return stringBuffer != null && stringBuffer.toString().equals("true");
    }

    /**
     * 设置链接,Post服务器并返回数据
     *
     * @param articleId 点赞的文章的ID
     * @return boolean    点赞成功返回true，反之，false
     */
    public boolean setPraise(String articleId) {
        boolean isSuccess = true;
        try {
            isSuccess = doPraisePost(UrlPraise, articleId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 设置链接,Post服务器并返回数据
     *
     * @param articleId 点赞的文章的ID
     * @return boolean    点赞成功返回true，反之，false
     */
    public boolean setPraiseGet(String articleId) {
        boolean isSuccess = true;
        try {
            isSuccess = doPraiseGet(UrlPraise + articleId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

}
