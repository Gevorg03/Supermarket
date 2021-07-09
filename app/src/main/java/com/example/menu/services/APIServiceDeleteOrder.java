package com.example.menu.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIServiceDeleteOrder {
    @POST("/api/post/deleteOrderAccount.php")
    @FormUrlEncoded
    Call<ResponseBody> deletePost(@Field("name") String name);
}
