package com.example.menu.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceUpdateProductMarket {
    @POST("/updateProductMarket1.php/")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("id") long id,
                                @Field("name") String name,
                                @Field("price") double price,
                                @Field("count") int count,
                                @Field("img") String img,
                                @Field("description") String type,
                                @Field("img_update") String img_update);
}
