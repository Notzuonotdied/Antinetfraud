package com.jiketuandui.antinetfraud.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Notzuonotdied on 2016/8/15.
 *
 *
 * 将UNIX时间转换为正常的时间
 */
public class transTime {
    /**
     * Java将Unix时间戳转换成指定格式日期
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        return new SimpleDateFormat(formats).format(
                new Date(Long.parseLong(timestampString) * 1000));
    }
}
