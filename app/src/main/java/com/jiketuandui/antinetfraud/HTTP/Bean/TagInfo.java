package com.jiketuandui.antinetfraud.HTTP.Bean;

/**
 * Created by Notzuonotdied on 2016/8/5.
 * tags为对象数组
 */
public class TagInfo {

    /**
     * id : 3
     * tag : 标签3
     * fid : 0
     */

    private String id;
    private String tag;
    private String fid;

    public void setId(String id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getFid() {
        return fid;
    }
}
