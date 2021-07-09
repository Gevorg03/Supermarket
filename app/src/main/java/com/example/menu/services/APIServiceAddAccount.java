package com.example.menu.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceAddAccount {
    @POST("/addContacts.php/")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("name") String name,
                                @Field("pass") String password);
}
