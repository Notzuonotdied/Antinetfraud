package com.jiketuandui.antinetfraud.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认返回信息格式
 *
 * @author wangyu
 */
@Data
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private String title;
    private T data;
    private T articles;
    private T article;
    private T notices;
    private String tag;

    public boolean isSuccess() {
        return code == 200;
    }

    public String toErrorString() {
        return "code=" + code + ", message=" + message;
    }

    public T getData() {
        if (articles != null) {
            return articles;
        } else if (article != null) {
            return article;
        } else if (notices != null) {
            return notices;
        }
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
