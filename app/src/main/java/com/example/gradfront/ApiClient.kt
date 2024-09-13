package com.example.gradfront

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://1.209.144.253/"

//    // OkHttpClient에 타임아웃 설정 추가
//    private val okHttpClient = OkHttpClient.Builder()
//        .connectTimeout(30, TimeUnit.SECONDS)  // 연결 타임아웃 30초로 설정
//        .readTimeout(30, TimeUnit.SECONDS)     // 읽기 타임아웃 30초로 설정
//        .writeTimeout(30, TimeUnit.SECONDS)    // 쓰기 타임아웃 30초로 설정
//        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
}