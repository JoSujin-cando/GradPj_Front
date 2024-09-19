package com.example.gradfront

import com.example.gradfront.data.BookingRequest
import com.example.gradfront.data.BookingResponse
import com.example.gradfront.data.KakaoPayReadyResponse
import com.example.gradfront.data.PayRequest
import com.example.gradfront.data.SongRecommendResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/kakao")
    fun sendKakaoAccessToken(@Body request: KakaoTokenRequest): Call<KakaoLoginResponse>

    @GET("/spotify/recommend")
    fun getSongRecommendations(@Query("artistName") artistName: String): Call<List<SongRecommendResponse>>

    @POST("booking/create")
    suspend fun createBooking(@Body request: BookingRequest): Response<BookingResponse>

    @POST("payment/ready")
    suspend fun readyPayment(@Body request: PayRequest): Response<KakaoPayReadyResponse>
}
