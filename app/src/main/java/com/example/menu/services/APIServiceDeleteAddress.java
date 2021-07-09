package com.example.menu.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIServiceDeleteAddress {
    @POST("/api/post/deleteProductAddress.php")
    @FormUrlEncoded
    Call<ResponseBody> deletePost(@Field("id") long id);
}
