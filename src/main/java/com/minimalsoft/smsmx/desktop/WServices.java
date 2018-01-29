/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minimalsoft.smsmx.desktop;

import com.minimalsoft.smsmx.desktop.models.BaseResponse;
import com.minimalsoft.smsmx.desktop.models.LoginRequest;
import com.minimalsoft.smsmx.desktop.models.LoginResposne;
import com.minimalsoft.smsmx.desktop.models.SendSmsRequest;
import com.minimalsoft.smsmx.desktop.models.SmsListResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 * @author DaveMorales
 */
public interface WServices {

    @Headers({"Content-Type:application/json", "Auth:Basic c21zTVg6c21zR2F0ZXdheTEyMyE="})
    @GET("/v1/report")
    Call<SmsListResponse> getSmsList(
            @Query("limit") String limit,
            @Query("offset") String offset,
            @Query("date") String date);

    @Headers({"Content-Type:application/json", "Auth:Basic c21zTVg6c21zR2F0ZXdheTEyMyE="})
    @POST("/v1/auth/login")
    Call<LoginResposne> loginUser(@Body LoginRequest body);
    
    @Headers({"Content-Type:application/json", "Auth:Basic c21zTVg6c21zR2F0ZXdheTEyMyE="})
    @POST("/v1/send")
    Call<BaseResponse> sendSMS(@Body String body);

}
