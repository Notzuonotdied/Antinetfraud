package com.jiketuandui.antinetfraud.retrofirt.service;


import com.jiketuandui.antinetfraud.entity.domain.AnnounceDetail;
import com.jiketuandui.antinetfraud.entity.domain.AnnounceList;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author wangyu
 * @describe 公告服务接口
 */
public interface AnnounceService {
    /**
     * 获取用户信息
     *
     * @param page 页号
     * @return 返回公告列表的监察者
     */
    @GET("/notice/all")
    Observable<Result<AnnounceList>> getAnnounceList(@Query("page") int page);

    /**
     * 获取用户信息
     *
     * @param announceId 公告ID
     * @return 返回公告ID的监察者
     */
    @GET("/notice/show/{id}")
    Observable<Result<AnnounceDetail>> getAnnounceDetail(@Path("id") int announceId);
}
