package com.example.gradfront

import com.example.gradfront.data.BookingRequest
import com.example.gradfront.data.BookingResponse
import com.example.gradfront.data.KakaoPayReadyResponse
import com.example.gradfront.data.KakaoTokenRequest
import com.example.gradfront.data.PayRequest
import com.example.gradfront.data.ClubData
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.SongRecommendResponse
import com.example.gradfront.data.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.*

interface ApiService {

    @POST("/oauth/kakao")
    fun sendKakaoAccessToken(@Body request: KakaoTokenRequest): Call<UserResponse>

    @GET("/spotify/recommend")
    fun getSongRecommendations(@Query("artistName") artistName: String): Call<List<SongRecommendResponse>>


    @POST("booking/create")
    suspend fun createBooking(@Body request: BookingRequest): Response<BookingResponse>

    @POST("payment/ready")
    suspend fun readyPayment(@Body request: PayRequest): Response<KakaoPayReadyResponse>

    @GET("/lives/today")
    fun getLiveData(): Call<List<LiveData>>

    @GET("/lives/all")
    fun getAllData(): Call<List<LiveData>>

    @GET("/booking/user/{userId}")
    fun getUserBookings(@Path("userId") userId: Long): Call<List<BookingResponse>>

    // liveId를 기반으로 개별 라이브 정보 조회
    @GET("/lives/{liveId}")
    fun getLiveDataById(@Path("liveId") liveId: Long): Call<LiveData>

    @GET("/clubs/{id}")
    fun getClubDataById(@Path("id") clubId: Long): Call<ClubData>

}
