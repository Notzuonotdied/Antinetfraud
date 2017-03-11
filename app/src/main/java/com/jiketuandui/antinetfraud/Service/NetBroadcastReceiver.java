package com.jiketuandui.antinetfraud.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import java.util.ArrayList;

public class NetBroadcastReceiver extends BroadcastReceiver {

    public static ArrayList<netEventHandler> mListeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话说明网络发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            MyApplication.mNetWorkState = NetWorkUtils.getNetWorkState(context);
            if (mListeners.size() > 0)// 通知接口完成加载
                for (netEventHandler handler : mListeners) {
                    handler.onNetChange();
                }
        }
    }

    public interface netEventHandler {
        void onNetChange();
    }
}
