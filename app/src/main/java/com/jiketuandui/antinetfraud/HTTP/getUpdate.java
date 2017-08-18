package com.jiketuandui.antinetfraud.HTTP;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jiketuandui.antinetfraud.Bean.UpdateInfo;
import com.jiketuandui.antinetfraud.Util.getVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 获取更新
 * Created by Notzuonotdied on 2017/3/7.
 */

public class getUpdate extends accessNetwork {

    /**
     * 常量，App更新下载链接
     */
    static String UrlgetApp = "/apk/";

    /**
     * 通过网址获取APP
     *
     * @param mURL App下载链接
     */
    static boolean getAPP(String mURL) {
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
     * 与服务器交互确定是否需要更新，通过提交的版本号，判断是否需要更新
     *
     * @param context Context
     */
    @NonNull
    public static String getUpdateString(Context context) {
        String path = "/api/update/" +
                getVersion.getVersionCode(context);
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = null;
        try {
            URL url = new URL(myUrl + path);
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
     * 处理getUpdateString获得的String，用Json String初始化UpdateInfo对象成员变量
     */
    @Nullable
    static UpdateInfo getUpdateInfo(Context context) {
        String json = getUpdateString(context);
        Gson gson = new Gson();
        Type type = new TypeToken<UpdateInfo>() {
        }.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
