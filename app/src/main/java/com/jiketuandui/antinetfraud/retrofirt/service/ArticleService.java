package com.jiketuandui.antinetfraud.retrofirt.service;


import com.jiketuandui.antinetfraud.entity.domain.ArticleDetail;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author wangyu
 * @describe 文章服务接口
 */
public interface ArticleService {
    /**
     * 获取案例的列表
     *
     * @param page 当前显示的页数
     * @return 返回案例列表的监察者
     */
    @GET("/article/all")
    Observable<Result<ArticleList>> getArticleList(@Query("page") int page);

    /**
     * 获取案例的列表
     *
     * @param page 当前显示的页数
     * @return 返回热门案例列表的监察者
     */
    @GET("/article/hot")
    Observable<Result<ArticleList>> getHotArticleList(@Query("page") int page);

    /**
     * 获取案例的详细信息
     *
     * @param articleId 案例ID
     * @return 返回案例详细内容的监察者
     */
    @GET("/article/show/{id}")
    Observable<Result<ArticleDetail>> getArticleDetail(@Path("id") int articleId);

    /**
     * 案例阅读量更新
     *
     * @param articleId 案例ID
     * @return 返回案例阅读量更新监察者
     */
    @POST("/article/read/{id}")
    Observable<Result<ArticleDetail>> updateReading(@Path("id") int articleId);

    /**
     * 案例点赞
     *
     * @param articleId 案例ID
     * @return 返回案例点赞监察者
     */
    @POST("/article/praise/{id}")
    Observable<Result<ArticleDetail>> praise(@Path("id") int articleId);


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
    Observable<Result<ArticleDetail>> collect(@Field("id") int userId,
                                              @Field("sign") String sign,
                                              @Field("timestamp") String timeStamp,
                                              @Field("article_id") String articleId);
}
