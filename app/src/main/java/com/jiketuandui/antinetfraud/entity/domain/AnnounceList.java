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
@NoArgsConstructor
public class AnnounceList {

    private int current_page;
    private int from;
    private int last_page;
    private Object next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<DataBean> data;

    @Data
    @NoArgsConstructor
    public static class DataBean {

        private int id;
        private String title;
        private String created_at;
    }
}
