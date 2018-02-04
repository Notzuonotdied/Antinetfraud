package com.jiketuandui.antinetfraud.entity.domain;

import lombok.Data;

/**
 * 这是公告的列表内容
 *
 * @author wangyu
 */
@Data
public class ArticleList extends BaseList<ArticleList.DataBean> {

    @Data
    public static class DataBean {

        private int id;
        private int tag_id;
        private String tag_name;
        private String title;
        private String image;
        private String source;
        private String content;
        private String created_at;
    }
}
