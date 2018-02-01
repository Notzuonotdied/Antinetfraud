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
public class ArticleDetail {

    private int id;
    private String title;
    private String image;
    private String source;
    private String content;
    private int reading;
    private int praise;
    private int tag_id;
    private String created_at;
    private String updated_at;

}
