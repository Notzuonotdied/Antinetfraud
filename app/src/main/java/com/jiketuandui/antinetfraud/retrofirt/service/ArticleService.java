package com.jiketuandui.antinetfraud.retrofirt.service;


import com.jiketuandui.antinetfraud.entity.domain.ArticleDetail;
import com.jiketuandui.antinetfraud.entity.domain.ArticleList;
import com.jiketuandui.antinetfraud.entity.domain.CommentList;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observable;
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
     * 分类案例返回
     *
     * @param tagId 标签ID
     * @param page  页码
     * @return 返回改标签下文章列表的监察者
     */
    @GET("article/tag/{id}")
    Observable<Result<ArticleList>> getArticleListByTag(@Path("id") int tagId,
                                                        @Query("page") int page);

    /**
     * 案例搜索
     *
     * @param keyWord 搜索关键字
     * @param page    页码
     * @return 返回关键字搜索返回文章列表的监察者
     */
    @GET("article/search/{keyword}")
    Observable<Result<ArticleList>> search(@Path("keyword") String keyWord,
                                           @Query("page") int page);

    /**
     * 获取案例评论列表
     *
     * @param articleId 评论文章ID
     * @param page      页号
     * @return 返回关键字搜索返回文章列表的监察者
     */
    @GET("/comment/show/{id}")
    Observable<Result<CommentList>> getCommentList(@Path("id") int articleId,
                                                   @Query("page") int page);
}
