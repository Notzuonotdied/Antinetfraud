package com.jiketuandui.antinetfraud.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Notzuonotdied on 2016/8/9.
 * 处理SharedPreferences
 */
public class SharedPManager {

    public static final String SHARE_XML_HISTORY = ".... .. ... - --- .-. -.-- ";
    public static final String SHARE_XML_COLLECTION = "-.-. --- .-.. .-.. . -.-. - .. --- -. ";
    private static final String SHARE_XML_NAME = "Anti_NET_Fraud";
    private SharedPreferences Sp;
    private SharedPreferences.Editor SpEditor;

    public SharedPManager(Context context) {
        Sp = context.getSharedPreferences(SHARE_XML_NAME, Context.MODE_PRIVATE);
        SpEditor = Sp.edit();
    }

    public SharedPManager(Context context, String XML_Name) {
        Sp = context.getSharedPreferences(XML_Name, Context.MODE_PRIVATE);
        SpEditor = Sp.edit();
    }

    public boolean isContains(String key) {
        return Sp.contains(key);
    }

    public String getString(String key, String defValue) {

        return Sp.getString(key, defValue);
    }

    public void clear() {
        SpEditor.clear();
    }

    public void putString(String key, String value) {
        SpEditor.putString(key, value);
    }

    public void remove(String key) {
        SpEditor.remove(key);
    }

    public boolean commit() {
        return SpEditor.commit();
    }

    public void apply() {
        SpEditor.apply();
    }

    /**
     * 添加Set数组
     */
    public void putStringSet(String key, Set<String> set) {
        SpEditor.putStringSet(key, set);
    }

    /**
     * 获取Set数组
     */
    public Set<String> getStringSet(String key, Set<String> set) {
        return Sp.getStringSet(key, set);
    }

}
