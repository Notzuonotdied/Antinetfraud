package com.jiketuandui.antinetfraud.retrofirt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiketuandui.antinetfraud.BuildConfig;
import com.jiketuandui.antinetfraud.retrofirt.serializer.DateDeserializer;
import com.jiketuandui.antinetfraud.retrofirt.service.AnnounceService;
import com.jiketuandui.antinetfraud.retrofirt.service.ArticleService;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.jiketuandui.antinetfraud.Util.AppConstants.DATA_FORMAT;

/**
 * 获取Retrofit Service工具类
 *
 * @author wangyu
 */

public class RetrofitServiceFactory {

    /**
     * API接口的地址
     */
    public static final String API_URL = "http://120.25.63.34";
    /**
     * 图床接口的地址
     */
    public static final String IMAGE_URL = "http://119.29.220.221";
    public static final AnnounceService ANNOUNCE_SERVICE =
            getHttpService(AnnounceService.class);
    public static final ArticleService ARTICLE_SERVICE =
            getHttpService(ArticleService.class);

    /**
     * 构建Http网络请求服务
     */
    private static <T> T getHttpService(final Class<T> service) {
        Gson gson = new GsonBuilder()
                .setDateFormat(DATA_FORMAT)
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(getClient(service))
                // 增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create(gson))
                // 增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }

    /**
     * 根据Service类型创建OKHttpClient
     *
     * @param service 服务类
     * @return 返回okHttp客户端
     */
    private static OkHttpClient getClient(final Class service) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            //设定日志级别
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }

        return builder.build();
    }
}
