package com.example.menu.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceOrderProduct {
    @POST("/addProduct.php/")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("name") String title,
                                @Field("address") String address,
                                @Field("count") int count,
                                @Field("price") double price,
                                @Field("ordering_name") String ordering_name,
                                @Field("ordering_phone") String ordering_phone,
                                @Field("img") String img);
}
