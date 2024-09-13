package com.example.gradfront

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/kakao")
    fun sendKakaoAccessToken(@Body request: KakaoTokenRequest): Call<KakaoLoginResponse>
}
