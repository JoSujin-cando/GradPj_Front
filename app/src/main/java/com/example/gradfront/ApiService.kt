package com.example.gradfront

import com.example.gradfront.data.BookingResponse
import com.example.gradfront.data.ClubData
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.SongRecommendResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/auth/kakao")
    fun sendKakaoAccessToken(@Body request: KakaoTokenRequest): Call<KakaoLoginResponse>

    @GET("/spotify/recommend")
    fun getSongRecommendations(@Query("artistName") artistName: String): Call<List<SongRecommendResponse>>

    @GET("/lives/today")
    fun getLiveData(): Call<List<LiveData>>

    @GET("/lives/all")
    fun getAllData(): Call<List<LiveData>>

    @GET("/booking/user/{userId}")
    fun getUserBookings(@Path("userId") userId: Long): Call<List<BookingResponse>>

    // liveId를 기반으로 개별 라이브 정보 조회
    @GET("/live/{liveId}")
    fun getLiveDataById(@Path("liveId") liveId: Long): Call<LiveData>

    @GET("/clubs/{id}")
    fun getClubDataById(@Path("id") clubId: Long): Call<ClubData>
}
