package com.jiketuandui.antinetfraud.entity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyu
 * @data 18-2-2
 * @describe 公告详情
 */
@Data
@NoArgsConstructor
public class AnnounceDetail {

    private int id;
    private String title;
    private String content;
    private String created_at;
    private String updated_at;
}
