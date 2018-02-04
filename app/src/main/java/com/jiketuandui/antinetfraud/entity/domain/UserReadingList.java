package com.jiketuandui.antinetfraud.entity.domain;

import lombok.Data;

/**
 * @author wangyu
 * @data 18-2-4
 * @describe TODO
 */
@Data
public class UserReadingList extends BaseList<UserReadingList.DataBean> {

    @Data
    public static class DataBean {

        private int id;
        private int user_id;
        private int article_id;
        private int tag_id;
        private String title;
        private String image;
        private String source;
        private String tag;
        private String updated_at;
    }
}
