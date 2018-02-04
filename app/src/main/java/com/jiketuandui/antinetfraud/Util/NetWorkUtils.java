package com.jiketuandui.antinetfraud.Util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.jiketuandui.antinetfraud.activity.util.NetWorkErrorActivity;

/**
 * 网络状态判断
 *
 * @author Notzuonotdied
 * @date 2016/10/31
 */
public class NetWorkUtils {
    public static final int NET_TYPE_NO_NETWORK = -1;
    private static final int NET_TYPE_WIFI = 1;
    private static final int NET_TYPE_MOBILE = 0;
    public Context context = null;

    public NetWorkUtils() {

    }

    public NetWorkUtils(Context context) {
        this.context = context;
    }

    public static MyApplication getApplication() {
        return MyApplication.getInstance();
    }

    /**
     * 判断是否联网
     */
    public static boolean isConnectNET(final Context context) {
        final ConnectivityManager conManage = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = conManage.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            ToastUtils.showShort("断网了，请检查网络~");
            return false;
        }
    }

    /**
     * 判断是否是WIFI连接
     */
    public static boolean isConnectWIFI() {
        final ConnectivityManager conManage = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conManage != null;
        NetworkInfo networkInfo = conManage.getActiveNetworkInfo();
        int netType = networkInfo != null ? networkInfo.getType() : -1;
        return netType == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected();
    }

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NET_TYPE_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NET_TYPE_MOBILE;
            }
        }
        return NET_TYPE_NO_NETWORK;
    }

    public static boolean isNetworkAviable(Context context) {
        int state = getNetWorkState(context);
        return state == 0 || state == 1;
    }

    public static void whenNetworkError() {
        Intent intent = new Intent();
        intent.setClass(getApplication(), NetWorkErrorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }
}
