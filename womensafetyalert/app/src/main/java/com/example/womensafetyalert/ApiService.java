package com.example.womensafetyalert;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    @POST("api/login")
    Call<ResponseBody> loginUser(@Body LoginRequest request);

    @POST("api/verify-otp")
    Call<ResponseBody> verifyOtp(@Body OtpRequest request);

    @POST("/api/resend-otp")
    Call<ResponseBody> resendOtp(@Body EmailRequest request);

    @POST("/api/get-user")
    Call<UserResponse> getUser(@Body Map<String, String> body);



}