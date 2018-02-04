package com.jiketuandui.antinetfraud.retrofirt.service;

import com.jiketuandui.antinetfraud.entity.domain.App;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
     * @param title   投稿的名称
     * @param type    类型
     * @param content 投稿的内容
     * @return 返回投稿的监察者
     */
    @POST("/contribution")
    Observable<Result<User>> contribute(@Field("title") String title,
                                        @Field("type") int type,
                                        @Field("content") String content);

    /**
     * app更新
     *
     * @return 返回APP更新的监察者
     */
    @GET("/app/latest")
    Observable<Result<App>> update();

    /**
     * 用户反馈
     *
     * @param content 用户反馈的内容
     * @return 返回用户反馈的情况的监察者
     */
    @POST("/feedback")
    @FormUrlEncoded
    Observable<Result<App>> feedback(@Field("content") String content);
}
