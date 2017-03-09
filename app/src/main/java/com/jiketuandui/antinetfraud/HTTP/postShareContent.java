package com.jiketuandui.antinetfraud.HTTP;

import android.text.TextUtils;

/**
 * 分享经历
 * Created by Notzuonotdied on 2017/3/7.
 */

public class postShareContent extends accessNetwork {
    /**
     * 设置链接,Post服务器并返回数据
     *
     * @param content 内容
     * @return 是否返回成功
     */
    public boolean post(String content) {
        try {
            String str = doPost("/api/share", content);
            if (TextUtils.equals(str, "true")) {
            //if (str.equals("true")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
