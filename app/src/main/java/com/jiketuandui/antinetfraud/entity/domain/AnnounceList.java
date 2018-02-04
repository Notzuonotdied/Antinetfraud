package com.jiketuandui.antinetfraud.entity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 这是公告的列表内容
 *
 * @author wangyu
 */
@Data
@NoArgsConstructor
public class AnnounceList extends BaseList<AnnounceList.DataBean> {

    @Data
    @NoArgsConstructor
    public static class DataBean {

        private int id;
        private String title;
        private String created_at;
    }
}
