package com.jiketuandui.antinetfraud.HTTP;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.ArticleContent;
import com.jiketuandui.antinetfraud.Bean.ListContent;
import com.jiketuandui.antinetfraud.Bean.TagInfo;
import com.jiketuandui.antinetfraud.Util.getVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    public static String myUrl = "http://119.29.220.221";
    /**
     * 常量,简介内容链接头部
     */
    public static String UrlContentHead = myUrl + "/?/api/article_list/";
    /**
     * 常量,热点的简介内容链接头部
     */
    public static String UrlContentHot = myUrl + "/?/api/article_hotlist/";
    /**
     * 常量，App更新下载链接
     */
    public static String UrlgetApp = myUrl + "/apk/";
    /**
     * 常量,文章内容连接头部
     */
    private static String UrlArticleHead = myUrl + "/?/api/view/";
    /**
     * 常量,搜索的简介内容链接头部
     */
    private static String UrlContentSearch = myUrl + "/?/api/search/";
    /**
     * 常量,点赞的链接头部
     */
    private static String UrlPraise = myUrl + "/?/api/praise/";

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


//            System.out.println(stringBuffer.toString());
//            File storeDir = new File("C:/Users/Notzuonotdied/Desktop/theAntiNETFraud");
//            if(!(storeDir.exists() && storeDir.isDirectory())){
//                storeDir.mkdirs();
//            }
//            BufferedOutputStream stream = null;
//            File file = null;
//            file = new File("C:/Users/Notzuonotdied/Desktop/theAntiNETFraud/newsData.json");
//            FileOutputStream fstream = new FileOutputStream(file);
//            stream = new BufferedOutputStream(fstream);
//            stream.write(stringBuffer.toString().getBytes());
//            stream.close();
//            fstream.close();
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
     * 点赞并获取返回结果
     *
     * @param mURL 地址
     * @return boolean 返回结果-是否成功
     * @throws IOException the io exception
     */
    public static boolean doPraiseGet(String mURL) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            url = new URL(mURL);
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
     * 将用户搜索的数据提交到服务器并获取服务器返回的数据
     * getListContentsBydoPost(UrlContentSearch + readingPage + "/8", inputString);
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
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(3000);
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

    /**
     * 将点赞提交到服务器并获取服务器返回的结果
     *
     * @param mURL      地址
     * @param articleId 点赞文章Id
     */
    private static Boolean doPraise(String mURL, String articleId) throws IOException {
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpURLConnection;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String line = null;
        try {
            url = new URL(mURL);
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
     * 通过网址获取APP
     *
     * @param mURL App下载链接
     */
    public static boolean getAPP(String mURL) {
        File file = null;
        InputStream is = null;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(mURL)
                    .openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setAllowUserInteraction(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(5000);
            is = httpURLConnection.getInputStream();
            FileOutputStream fileOutputStream;
            if (is != null) {
                file = new File(
                        Environment.getExternalStorageDirectory().getPath(),
                        "网络诈骗防范科普网.apk");
                fileOutputStream = new FileOutputStream(file);
                byte[] buf = new byte[512];
                int ch = -1;
                while ((ch = is.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, ch);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    httpURLConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file != null;
    }

    /**
     * 与服务器交互确定是否需要更新
     *
     * @param context Context
     */
    public static String getUpdateInfo(Context context) {
        String path = myUrl + "/?/api/update/" +
                getVersion.getVersionCode(context);
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = null;
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestProperty("Accept-Charset", "utf-8");
            urlConnection.setAllowUserInteraction(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(5000);
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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
     * @param pager   页数
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

    /**
     * 设置链接,Post服务器并返回数据
     *
     * @param articleId 点赞的文章的ID
     * @return boolean    点赞成功返回true，反之，false
     */
    public static boolean setPraise(String articleId) {
        boolean isSuccess = true;
        try {
            isSuccess = doPraise(UrlPraise, articleId);
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
    public static boolean setPraiseGet(String articleId) {
        boolean isSuccess = true;
        try {
            isSuccess = doPraiseGet(UrlPraise + articleId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

}
