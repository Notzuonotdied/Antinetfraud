package com.jiketuandui.antinetfraud.retrofirt.service;

import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * @author wangyu
 * @data 18-2-2
 * @describe TODO
 */

public interface OtherService {
    /**
     * 投稿
     *
     * @param title  投稿的名称
     * @param type   类型
     * @param content 投稿的内容
     * @return 返回投稿的监察者
     */
    @POST("/contribution")
    Observable<Result<User>> contribute(@Field("title") String title,
                                        @Field("type") int type,
                                        @Field("content") String content);
}
