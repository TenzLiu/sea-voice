package com.siyecaoi.yy.netUtls;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/1/25.
 */

public interface ApiServics {

    @GET("")
    Observable<ResponseBody> getResult(@Url String url, @HeaderMap Map<String, String> headerParams, @QueryMap Map<String, Object> params);

    @GET("")
    Observable<ResponseBody> getResult(@Url String url, @HeaderMap Map<String, String> headerParams);

    @FormUrlEncoded
    @POST("")
    Observable<ResponseBody> postResult(@Url String url, @FieldMap Map<String, Object> params);


    @POST("{action}")
    Observable<ResponseBody> postResultll(@Path("action") String action, @Body RequestBody requestBody);

    @Multipart
    @POST("")
    Observable<ResponseBody> upload(@Url String url, @Part MultipartBody.Part file);
}
