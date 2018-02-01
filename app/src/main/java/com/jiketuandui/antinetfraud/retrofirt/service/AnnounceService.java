package com.jiketuandui.antinetfraud.retrofirt.service;


import com.jiketuandui.antinetfraud.entity.domain.AnnounceList;
import com.jiketuandui.antinetfraud.entity.domain.User;
import com.jiketuandui.antinetfraud.entity.dto.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author wangyu
 * @describe 公告服务接口
 */
public interface AnnounceService {
    /**
     * 获取用户信息
     *
     * @return 返回公告列表的监察者
     */
    @GET("/notice/all")
    Observable<Result<AnnounceList>> getAnnounceList();

    /**
     * 获取用户信息
     *
     * @param announceId 公告ID
     * @return 返回公告ID的监察者
     */
    @GET("/notice/show/{id}")
    Observable<Result<User>> getAnnounceDetail(@Path("id") String announceId);
}
