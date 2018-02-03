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
    private T user;
    private T articles;
    private T article;
    private T notices;
    private T info;
    private T notice;
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
        } else if (user != null) {
            return user;
        } else if (info != null) {
            return info;
        } else if (notice != null) {
            return notice;
        }
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
