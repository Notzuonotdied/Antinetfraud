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

    /**
     * 是否包含特定key的数据
     * @param key 查找Key值对应的数据
     * */
    public boolean isContains(String key) {
        return Sp.contains(key);
    }

    /**
     * 获取Key的String数据
     * @param key 键值
     * @param defValue 假如没有找到，就返回defValue
     * */
    public String getString(String key,String defValue) {
        return Sp.getString(key,defValue);
    }

    /**
     * 清空所有的数据
     * */
    public void clear() {
        SpEditor.clear();
    }

    /**
     * 向SharePreferences存入指定key对应的数据
     * @param key 键值
     * @param value 键值对应的数据
     * */
    public void putString(String key,String value) {
        SpEditor.putString(key,value);
    }

    /**
     * 删除SharePrefences里指定key对应的数据项
     * @param key 键值
     * */
    public void remove(String key) {
        SpEditor.remove(key);
    }

    /**
     * 当修改完之后，需要进行提交修改
     * */
    public boolean commit() {
        return SpEditor.commit();
    }
}
