package com.jiketuandui.antinetfraud.entity.domain;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangyu
 * @data 18-2-3
 * @describe 数据基类
 */
@Data
@NoArgsConstructor
public class BaseList<T> {
    protected int current_page;
    protected int last_page;
    protected int from;
    protected int to;
    protected int total;
    protected int per_page;
    protected String path;
    protected String prev_page_url;
    protected String next_page_url;

    protected List<T> data;
}
