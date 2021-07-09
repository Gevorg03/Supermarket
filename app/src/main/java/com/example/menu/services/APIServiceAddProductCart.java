package com.example.menu.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceAddProductCart {
    @POST("/addProductsCart.php/")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("userName") String user_name,
                                @Field("name") String name,
                                @Field("count") int count,
                                @Field("price") double price,
                                @Field("img") String img);
}

