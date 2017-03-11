package com.jiketuandui.antinetfraud.HTTP;

import android.support.annotation.Nullable;

import com.jiketuandui.antinetfraud.Service.NetBroadcastReceiver;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

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
 * 网络连接核心部分
 * Created by Notzuonotdied on 2017/3/7.
 */
class accessNetwork  {
    static final String myUrl = "http://119.29.220.221";

    /**
     * 获取Json数组
     *
     * @param mURL 地址
     * @return Json数组
     * @throws IOException the io exception
     */
    @Nullable
    String doGet(String mURL) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            /* *
             * 使用HttpURLConnection的步骤是先实例化一个URL对象，通过URL的openConnection实例化
             * HttpURLConnection对象。然后设置参数，注意此时并没有发生连接。真正发生连接是在获得流
             * 时即conn.getInputStream这一句时，这点跟TCP Socket是一样的。并非阻塞在
             * ServerSocket.accept()而是阻塞在获取流。所以在获取流之前应该设置好所有的参数。
             * */
            url = new URL(myUrl + mURL);
            /* *
             * 此处的urlConnection对象实际上是根据URL的
             * 请求协议(此处是http)生成的URLConnection类
             * 的子类HttpURLConnection,故此处最好将其转化
             * 为HttpURLConnection类型的对象,以便用到
             * HttpURLConnection更多的API.如下:
             */
            urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod("GET");    //使用的http的get方法
            //要是conn.getResponseCode()的值为200，再进行后面的操作。
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
     * getListContentsBydoPost(UrlContentSearch + readingPage + "/8", inputString);
     *
     * @param mURL        地址
     * @param inputString 搜索的内容
     */
    @Nullable
    String doPost(String mURL, String inputString) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            /* *
             * 使用HttpURLConnection的步骤是先实例化一个URL对象，通过URL的openConnection实例化
             * HttpURLConnection对象。然后设置参数，注意此时并没有发生连接。真正发生连接是在获得流
             * 时即conn.getInputStream这一句时，这点跟TCP Socket是一样的。并非阻塞在
             * ServerSocket.accept()而是阻塞在获取流。所以在获取流之前应该设置好所有的参数。
             * */
            url = new URL(myUrl + mURL);
            /* *
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
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.connect();

            PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
//            if (httpURLConnection.getResponseCode() == 200) {
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
//            } else {
//                Toast.makeText(MyApplication.getInstance().getApplicationContext(),
//                        "网络不畅通~", Toast.LENGTH_SHORT).show();
            //   }

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
}
