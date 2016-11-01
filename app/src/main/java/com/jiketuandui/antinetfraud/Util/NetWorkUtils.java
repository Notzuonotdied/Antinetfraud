package com.jiketuandui.antinetfraud.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Notzuonotdied on 2016/10/31.
 * 网络状态判断
 */
public class NetWorkUtils {
    public static final String NET_TYPE_WIFI = "WIFI";
    public static final String NET_TYPE_MOBILE = "MOBILE";
    public static final String NET_TYPE_NO_NETWORK = "no_network";

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
            Toast.makeText(context, "断网了，请检查网络~", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 判断是否是WIFI连接
     * */
    public static boolean isConnectWIFI() {
        final ConnectivityManager conManage = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManage.getActiveNetworkInfo();
        int netType = networkInfo != null ? networkInfo.getType() : -1;
        return netType == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected();
    }

}
