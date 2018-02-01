package com.jiketuandui.antinetfraud.HTTP.Bean;

/**
 * 用户更新的API的属性
 * Created by wangyu on 17-3-21.
 */

public class UpdateInfo {

    /**
     * version : 4、版本号
     * update_log : 重大更新、这个参数表示更新的内容
     * update_time : 1490097887、UNIX时间戳
     * url : http://127.0.0.1/apk/antinetfraud_4.apk、下载地址
     */

    private String version;
    private String update_log;
    private String update_time;
    private String url;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getUrl() {
        return url;
    }
}
