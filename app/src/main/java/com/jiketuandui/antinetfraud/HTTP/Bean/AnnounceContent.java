package com.jiketuandui.antinetfraud.HTTP.Bean;

/**
 * 公告类
 * Created by Notzuonotdied on 2017/3/8.
 */

public class AnnounceContent {
    private String id;
    private String title;
    private String content;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
