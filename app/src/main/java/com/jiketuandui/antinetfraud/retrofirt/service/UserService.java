package com.jiketuandui.antinetfraud.retrofirt.service;


import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author wangyu
 * @describe 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param name       用户名称
     * @param password   用户密码
     * @param pswConfirm 用户密码确认
     * @param email      用户验证邮箱
     * @return 返回用户注册的信息监察者
     */
    @POST("/auth/register")
    @FormUrlEncoded
    Observable<Result<User>> register(@Field("name") String name,
                                      @Field("password") String password,
                                      @Field("password_confirmation") String pswConfirm,
                                      @Field("email") String email);

    /**
     * 用户登录
     *
     * @param name     用户名称
     * @param password 用户密码
     * @return 返回用户登录的信息监察者
     */
    @POST("auth/login")
    @FormUrlEncoded
    Observable<Result<User>> login(@Field("name") String name,
                                   @Field("password") String password);

    /**
     * 用户登出
     *
     * @param id        用户ID
     * @param sign      用户签名验证
     * @param timestamp 当前时间
     * @return 返回用户登出的信息监察者
     */
    @POST("/auth/logout")
    @FormUrlEncoded
    Observable<Result<User>> logout(@Field("id") String id,
                                    @Field("sign") String sign,
                                    @Field("timestamp") String timestamp);

    /**
     * 检查邮箱是否有效
     *
     * @param email 用户邮箱
     * @return 返回检查用户邮箱是否有效的监察者
     */
    @GET("auth/check/email")
    Observable<Result<User>> isEmailValid(@Query("email") String email);

    /**
     * 检查用户名是否有效
     *
     * @param name 用户名
     * @return 返回检查用户名是否有效的监察者
     */
    @GET("/auth/check/name")
    Observable<Result<User>> isNameValid(@Query("name") String name);

    /**
     * 获取用户的浏览历史
     *
     * @param id        用户ID
     * @param sign      签名凭证
     * @param timestamp 当前时间
     * @return 返回用户浏览历史的监察者
     */
    @GET("/auth/history/")
    Observable<Result<ArticleList>> getHistory(@Field("id") String id,
                                               @Field("sign") String sign,
                                               @Field("timestamp") long timestamp);

    /**
     * 案例收藏
     *
     * @param userId    用户ID
     * @param sign      签名凭证
     * @param timeStamp 时间戳
     * @param articleId 文章ID
     * @return 返回案例收藏监察者
     */
    @POST("/auth/article/collection")
    @FormUrlEncoded
    Observable<Result<ArticleList>> collect(@Field("id") String userId,
                                            @Field("sign") String sign,
                                            @Field("timestamp") long timeStamp,
                                            @Field("article_id") int articleId);

    /**
     * 收藏文章列表
     *
     * @param id        用户ID
     * @param sign      签名凭证
     * @param timestamp 当前时间
     * @return 返回用户收藏列表的监察者
     */
    @GET("/auth/collection/")
    Observable<Result<ArticleList>> getCollection(@Query("id") String id,
                                                  @Query("sign") String sign,
                                                  @Query("timestamp") long timestamp);

    /**
     * 用户评论
     *
     * @param id        用户ID
     * @param sign      签名凭证
     * @param timestamp 当前时间戳
     * @param articleId 文章ID
     * @param content   评论内容
     * @return 返回用户收藏列表的监察者
     */
    @POST("/auth/article/comment")
    @FormUrlEncoded
    Observable<Result<User>> comment(@Field("id") String id,
                                     @Field("sign") String sign,
                                     @Field("timestamp") long timestamp,
                                     @Field("article_id") int articleId,
                                     @Field("content") String content);
}
