package com.jiketuandui.antinetfraud.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Notzuonotdied on 2016/8/9.
 * 处理SharedPreferences
 */
public class SharedPManager {
    public static final String SHARE_XML_NAME = "Anti_NET_Fraud";

    private SharedPreferences Sp;
    private SharedPreferences.Editor SpEditor;

    public SharedPManager(Context context) {
        Sp = context.getSharedPreferences(SHARE_XML_NAME,Context.MODE_PRIVATE);
    }

}
