package com.jiketuandui.antinetfraud.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * 是否包含特定key的数据
     *
     * @param key 查找Key值对应的数据
     */
    public boolean isContains(String key) {
        return Sp.contains(key);
    }

    /**
     * 获取Key的String数据
     *
     * @param key      键值
     * @param defValue 假如没有找到，就返回defValue
     */
    public String getString(String key, String defValue) {
        return Sp.getString(key, defValue);
    }

    /**
     * 清空所有的数据
     */
    public void clear() {
        SpEditor.clear();
    }

    /**
     * 向SharePreferences存入指定key对应的数据
     *
     * @param key   键值
     * @param value 键值对应的数据
     */
    public void putString(String key, String value) {
        SpEditor.putString(key, value);
    }

    /**
     * 删除SharePrefences里指定key对应的数据项
     *
     * @param key 键值
     */
    public void remove(String key) {
        SpEditor.remove(key);
    }

    /**
     * 当修改完之后，需要进行提交修改
     */
    public boolean commit() {
        return SpEditor.commit();
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

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        SpEditor.clear();
        SpEditor.putString(tag, strJson);
        SpEditor.commit();
    }

    /**
     * 获取List
     *
     * @param tag 数据的名称
     * @return 返回List
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = Sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;
    }
}
