package com.jiketuandui.antinetfraud.HTTP;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.MaterialDialog;
import com.jiketuandui.antinetfraud.HTTP.Bean.UpdateInfo;
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
        final MaterialDialog dialog = new MaterialDialog(context);
        String btnInfo = "确定";
        if (isVisible) {
            btnInfo = "更新";
        }
        dialog // 设置Dialog的属性
                .title("更新通知")
                .btnNum(1)
                .content(updateInfo)// 设置内容
                .btnText(btnInfo)// 设置按钮文本
                .showAnim(new BounceTopEnter())// 设置进入动画
                .dismissAnim(new SlideBottomExit())// 设置退出动画
                .show();
        dialog.setOnBtnClickL(
                () -> {
                    if (isVisible) {
                        if (Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {
                            dialog.dismiss();
                            new getAPPTask().execute(mUrl);
                        } else {
                            Toast.makeText(context, "SD卡不可用，请插入SD卡",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                    }
                }
        );
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
     */
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
