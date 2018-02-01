package com.jiketuandui.antinetfraud.entity.domain;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 这是公告的列表内容
 *
 * @author wangyu
 */
@Data
public class ArticleList {

    private int current_page;
    private int from;
    private int last_page;
    private String next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<DataBean> data;

    @Data
    public static class DataBean {

        private int id;
        private String title;
        private String image;
        private String source;
        private String content;
        private int tag_id;
        private String tag_name;
        private String created_at;
    }
}
