package com.example.menu.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceUpdateProductAddress {
    @POST("/updateProductAddress.php/")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("id") long id,
                                @Field("name") String name,
                                @Field("address") String address,
                                @Field("phone") String phone);
}