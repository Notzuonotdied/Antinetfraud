package com.jiketuandui.antinetfraud.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.jiketuandui.antinetfraud.Util.MyApplication;
import com.jiketuandui.antinetfraud.Util.NetWorkUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 网络状态接受
 *
 * @author wangyu
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    public static ArrayList<NetEventHandler> mListeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话说明网络发生了变化
        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            MyApplication.NetWorkState = NetWorkUtils.getNetWorkState(context);
            // 通知接口完成加载
            if (mListeners.size() <= 0) {
                return;
            }
            for (NetEventHandler handler : mListeners) {
                handler.onNetChange();
            }
        }
    }

    public interface NetEventHandler {
        /**
         * 当网络变化的时候回调
         */
        void onNetChange();
    }
}
