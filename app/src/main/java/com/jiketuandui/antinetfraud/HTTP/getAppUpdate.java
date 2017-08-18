package com.jiketuandui.antinetfraud.HTTP;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.Bean.UpdateInfo;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.transTime;

import java.io.File;

/**
 * Created by Notzuonotdied on 2016/11/20.
 * 判断是否有更新
 */
public class getAppUpdate {
    private Context context;
    private String mUrl;
    private boolean isShow;

    public getAppUpdate(Context context, boolean isShow) {
        this.isShow = isShow;
        this.context = context;
    }

    public void startUpdate() {
        new NeedRefreshTask().execute();
    }

    //显示公告
    private void showAnnounceDialog(String updateInfo, boolean isVisible) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        Window window = alertDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setContentView(R.layout.announcement_update);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
        tv_title.setText("更新通知");
        TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
        tv_message.setText(updateInfo);
        LinearLayout btn_bottom = (LinearLayout) window.findViewById(R.id.btn_bottom);
        if (!isVisible) {
            btn_bottom.setVisibility(View.GONE);
        }
        TextView tv_cancel = (TextView) window.findViewById(R.id.tv_dialog_cancel);
        tv_cancel.setOnClickListener(view -> alertDialog.dismiss());
        TextView tv_confirm = (TextView) window.findViewById(R.id.tv_dialog_confirm);
        tv_confirm.setOnClickListener(view -> {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                alertDialog.cancel();
                new getAPPTask().execute(mUrl);
            } else {
                Toast.makeText(context, "SD卡不可用，请插入SD卡",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 安装App
     */
    private void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "Test.apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    private class NeedRefreshTask extends AsyncTask<Void, Void, UpdateInfo> {

        @Override
        protected UpdateInfo doInBackground(Void... voids) {
            return getUpdate.getUpdateInfo(context);
        }

        @Override
        protected void onPostExecute(UpdateInfo updateInfo) {
            if (updateInfo == null) {
                if (isShow) {
                    showAnnounceDialog("\u3000当前已经是最新版了～", false);
                }
            } else {
                // 处理获取到的URL，因为URL中包含了\/，要去掉一个\才可以
                mUrl = getUpdate.UrlgetApp + updateInfo.getUrl().split("/")[4].replace("\"", "");
                Log.i("Notzuonotdied", mUrl);
                showAnnounceDialog("\u3000有最新版可以下载～\n" +
                        "\u3000更新时间：" + transTime.getTime(updateInfo.getUpdate_time())
                        + "\n\u3000版本号：" + updateInfo.getVersion() +
                        "\n\u3000更新内容：" + updateInfo.getUpdate_log(), true);
            }
            super.onPostExecute(updateInfo);
        }
    }

    /**
     * 下载APP
     * */
    private class getAPPTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return getUpdate.getAPP(accessNetwork.myUrl + strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean != null && aBoolean) {
                // 安装APP
                update();
            }
            super.onPostExecute(aBoolean);
        }
    }
}
