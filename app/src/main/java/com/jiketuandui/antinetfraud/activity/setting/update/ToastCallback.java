package com.jiketuandui.antinetfraud.activity.setting.update;

import android.content.Context;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;

import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.base.DownloadCallback;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;

/**
 * @author haoge on 2018/1/9.
 */

public class ToastCallback implements CheckCallback, DownloadCallback {

    @Override
    public void onCheckStart() {
        ToastUtils.showShort("启动更新任务");
    }

    @Override
    public void hasUpdate(Update update) {
        ToastUtils.showShort("检测到有更新");
    }

    @Override
    public void noUpdate() {
        ToastUtils.showShort("检测到没有更新");
    }

    @Override
    public void onCheckError(Throwable t) {
        ToastUtils.showShort("更新检查失败：" + t.getMessage());
    }

    @Override
    public void onUserCancel() {
        ToastUtils.showShort("用户取消更新");
    }

    @Override
    public void onCheckIgnore(Update update) {
        ToastUtils.showShort("用户忽略此版本更新");
    }

    @Override
    public void onDownloadStart() {
        ToastUtils.showShort("开始下载");
    }

    @Override
    public void onDownloadComplete(File file) {
        ToastUtils.showShort("下载完成");
    }

    @Override
    public void onDownloadProgress(long current, long total) {

    }

    @Override
    public void onDownloadError(Throwable t) {
        ToastUtils.showShort("下载失败");
    }
}
