package com.jiketuandui.antinetfraud.HTTP;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.R;

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
    public void showAnnouncmentDialog(String updataInfo, boolean isVisible) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.announcement_update);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
        tv_title.setText("更新通知");
        TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
        tv_message.setText(updataInfo);
        LinearLayout btn_bottom = (LinearLayout) window.findViewById(R.id.btn_bottom);
        if (!isVisible) {
            btn_bottom.setVisibility(View.GONE);
        }
        TextView tv_cancel = (TextView) window.findViewById(R.id.tv_dialog_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        TextView tv_confirm = (TextView) window.findViewById(R.id.tv_dialog_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    new getAPPTask().execute(mUrl);
                } else {
                    Toast.makeText(context, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 安装App
     */
    public void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "Test.apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private class NeedRefreshTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return getConnect.getUpdateInfo(context);
        }

        @Override
        protected void onPostExecute(String string) {
            if ("null".equals(string) || string == null || string.length() <= 0 ||
                    string.isEmpty() || "".equals(string)) {
                if (isShow) {
                    showAnnouncmentDialog("\u3000已经是最新版了~", false);
                }
            } else {
                mUrl = getConnect.UrlgetApp + string.split("/")[4].replace("\"", "");
                showAnnouncmentDialog("\u3000有最新版可以下载~", true);
            }
            super.onPostExecute(string);
        }
    }

    private class getAPPTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return getConnect.getAPP(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                update();
            }
            super.onPostExecute(aBoolean);
        }
    }
}
