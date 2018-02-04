package com.jiketuandui.antinetfraud.activity.setting.update;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.PermissionUtils;
import com.google.gson.Gson;
import com.jiketuandui.antinetfraud.entity.domain.App;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.model.Update;

/**
 * 判断是否有更新
 *
 * @author Notzuonotdied
 * @date 2016/11/20
 */
public class getAppUpdate {
    private boolean isPermissionGrant;
    private ToastCallback callback;
    private boolean isOpenToast;

    public getAppUpdate(Context context, boolean isOpenToast) {
        requestStoragePermission();
        callback = new ToastCallback(context);
        this.isOpenToast = isOpenToast;
    }

    public void startUpdate() {
        isPermissionGrant = false;
        createBuilder().check();
    }

    /**
     * 请求文件读写权限。
     */
    private void requestStoragePermission() {
        if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            isPermissionGrant = true;
            PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @NonNull
    private UpdateBuilder createBuilder() {
        // 配置toast通知的回调
        UpdateBuilder builder = UpdateBuilder.create(createNewConfig());
        if (isOpenToast) {
            builder.setCheckCallback(callback);
            builder.setDownloadCallback(callback);
        }
        builder.setCheckWorker(OkhttpCheckWorker.class);
        builder.setDownloadWorker(OkhttpDownloadWorker.class);
        builder.setUpdateStrategy(new AllDialogShowStrategy());
        if (isPermissionGrant) {
            builder.setFileCreator(new CustomApkFileCreator());
        }
        return builder;
    }

    private UpdateConfig createNewConfig() {
        return UpdateConfig.createConfig()
                .setUrl("http://120.25.63.34/app/latest")
                .setUpdateParser(httpResponse -> {
                    App app = new Gson().fromJson(httpResponse, App.class);
                    Update update = new Update();
                    // 此apk包的下载地址
                    update.setUpdateUrl(app.getApp().getDownload());
                    // 此apk包的版本名称
                    update.setVersionName(app.getApp().getVersion());
                    // 此apk包的更新内容
                    update.setUpdateContent(app.getApp().getUpdate_log());
                    // 此apk包是否为强制更新
                    update.setForced(true);
                    return update;
                });
    }

}
