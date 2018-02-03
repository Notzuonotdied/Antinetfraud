package com.jiketuandui.antinetfraud.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Locale.US;

/**
 * Created by Notzuonotdied on 2016/8/15.
 * 将UNIX时间转换为正常的时间
 */
public class transTime {
    /**
     * Java将Unix时间戳转换成指定格式日期
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        String time = null;
        if (timestampString != null && !timestampString.contains("年") && formats != null) {
            time = new SimpleDateFormat(formats, US).format(new Date(Long.parseLong(timestampString) * 1000));
        }
        return time;
    }

    public static String getTime(String createtime) {
        return transTime.TimeStamp2Date(createtime, "yyyy") + "年" +
                transTime.TimeStamp2Date(createtime, "MM") + "月" +
                transTime.TimeStamp2Date(createtime, "dd") + "日";
    }
}
