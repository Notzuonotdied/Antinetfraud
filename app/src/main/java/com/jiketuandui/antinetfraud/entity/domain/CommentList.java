package com.jiketuandui.antinetfraud.entity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyu
 * @data 18-2-3
 * @describe TODO
 */
@Data
@NoArgsConstructor
public class CommentList extends BaseList<CommentList.DataBean> {

    private String first_page_url;
    private String last_page_url;

    @Data
    @NoArgsConstructor
    public static class DataBean {

        private int id;
        private String user_name;
        private String content;
        private String author_reply;
        private String created_at;
    }
}
