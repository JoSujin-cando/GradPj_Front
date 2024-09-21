package com.example.gradfront

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradfront.data.BookingResponse
import com.example.gradfront.data.ClubData
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.LiveDataWithClub
import com.example.gradfront.databinding.ActivityMyPageBinding
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPage : AppCompatActivity() {

    lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutBtn.setOnClickListener {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }

        // RecyclerView 설정
        binding.myRv.layoutManager = LinearLayoutManager(this)

        // *Retrofit을 통해 데이터를 불러옴*
        fetchLiveData()

        //itemDecoration
        binding.myRv.addItemDecoration(SpacingItem(20))
    }

    private fun fetchLiveData() {
        val userId: Long = 1 // 예시로 고정값, 실제로는 로그인된 사용자의 ID로 대체
        ApiClient.getApiService().getUserBookings(userId)
            .enqueue(object : Callback<List<BookingResponse>> {
                override fun onResponse(
                    call: Call<List<BookingResponse>>,
                    response: Response<List<BookingResponse>>
                ) {
                    if (response.isSuccessful) {
                        val bookingList = response.body() ?: emptyList()
                        val liveDataList = mutableListOf<Pair<BookingResponse, LiveDataWithClub?>>()
                        var completedRequests = 0

                        bookingList.forEach { booking ->
                            ApiClient.getApiService().getLiveDataById(booking.liveId)
                                .enqueue(object : Callback<LiveData> {
                                    override fun onResponse(
                                        call: Call<LiveData>,
                                        liveResponse: Response<LiveData>
                                    ) {
                                        if (liveResponse.isSuccessful) {
                                            val liveData = liveResponse.body()
                                            liveData?.let {
                                                // club_id로 clubName을 가져옴
                                                ApiClient.getApiService().getClubDataById(it.club_id)
                                                    .enqueue(object : Callback<ClubData> {
                                                        override fun onResponse(
                                                            call: Call<ClubData>,
                                                            clubResponse: Response<ClubData>
                                                        ) {
                                                            if (clubResponse.isSuccessful) {
                                                                val clubName = clubResponse.body()?.clubName ?: "Unknown Club"
                                                                liveDataList.add(Pair(booking, LiveDataWithClub(it, clubName)))
                                                            } else {
                                                                liveDataList.add(Pair(booking, LiveDataWithClub(it, "Unknown Club")))
                                                            }
                                                            completedRequests++
                                                            if (completedRequests == bookingList.size) {
                                                                setUpRecyclerView(liveDataList)
                                                            }
                                                        }

                                                        override fun onFailure(call: Call<ClubData>, t: Throwable) {
                                                            liveDataList.add(Pair(booking, LiveDataWithClub(it, "Unknown Club")))
                                                            completedRequests++
                                                            if (completedRequests == bookingList.size) {
                                                                setUpRecyclerView(liveDataList)
                                                            }
                                                        }
                                                    })
                                            }
                                        } else {
                                            liveDataList.add(Pair(booking, null))
                                            completedRequests++
                                            if (completedRequests == bookingList.size) {
                                                setUpRecyclerView(liveDataList)
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<LiveData>, t: Throwable) {
                                        liveDataList.add(Pair(booking, null))
                                        completedRequests++
                                        if (completedRequests == bookingList.size) {
                                            setUpRecyclerView(liveDataList)
                                        }
                                    }
                                })
                        }
                    } else {
                        Toast.makeText(this@MyPage, "Failed to load booking data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<BookingResponse>>, t: Throwable) {
                    Toast.makeText(this@MyPage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


//    /**/
//    private fun fetchLiveData() {
//        // 유저 ID는 로그인된 사용자 정보에서 받아오도록 설정
//        val userId: Long = 1 // 예시로 고정값, 실제로는 로그인된 사용자의 ID로 대체
//
//        ApiClient.getApiService().getUserBookings(userId)
//            .enqueue(object : Callback<List<BookingResponse>> {
//                override fun onResponse(
//                    call: Call<List<BookingResponse>>,
//                    response: Response<List<BookingResponse>>
//                ) {
//                    if (response.isSuccessful) {
//                        val bookingList = response.body() ?: emptyList()
//                        Log.d("mypage", bookingList.toString())
//
//                        // 각 예약 정보에서 라이브 정보를 추가로 불러옴
//                        val liveDataList = mutableListOf<Pair<BookingResponse, LiveData?>>()
//                        var completedRequests = 0 // 완료된 API 요청 수
//
//                        bookingList.forEach { booking ->
//                            ApiClient.getApiService().getLiveDataById(booking.liveId)
//                                .enqueue(object : Callback<LiveData> {
//                                    override fun onResponse(
//                                        call: Call<LiveData>,
//                                        liveResponse: Response<LiveData>
//                                    ) {
//                                        if (liveResponse.isSuccessful) {
//                                            val liveData = liveResponse.body()
//                                            liveDataList.add(Pair(booking, liveData))
//                                        } else {
//                                            liveDataList.add(Pair(booking, null)) // 실패 시 null 처리
//                                        }
//                                        completedRequests++
//                                        // 모든 API 요청이 완료되었을 때 Adapter 설정
//                                        if (completedRequests == bookingList.size) {
//                                            setUpRecyclerView(liveDataList)
//                                        }
//                                    }
//
//                                    override fun onFailure(call: Call<LiveData>, t: Throwable) {
//                                        liveDataList.add(Pair(booking, null)) // 실패 시 null 처리
//                                        completedRequests++
//
//                                        // 모든 API 요청이 완료되었을 때 Adapter 설정
//                                        if (completedRequests == bookingList.size) {
//                                            setUpRecyclerView(liveDataList)
//                                        }
//                                    }
//                                })
//                        }
//                    } else {
//                        Toast.makeText(
//                            this@MyPage,
//                            "Failed to load booking data",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<List<BookingResponse>>, t: Throwable) {
//                    Toast.makeText(this@MyPage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }

    // RecyclerView 설정을 따로 함수로 분리
    private fun setUpRecyclerView(liveDataList: List<Pair<BookingResponse, LiveDataWithClub?>>) {
        val adapter = MyPageAdapter(liveDataList) { booking, live ->
            val intent = Intent(this@MyPage, BookingPage::class.java).apply {
                putExtra("title", live?.liveData?.title)
                putExtra("bookingDate", booking.bookingDate)
                putExtra("date", live?.liveData?.date)
                putExtra("place", live?.liveData?.club_id)
                putExtra("totalAmount", booking.totalAmount)
                putExtra("timetable", live?.liveData?.timetable)
                putExtra("notice", live?.liveData?.notice)
                putExtra("imageResId", live?.liveData?.image)
                putExtra("numberOfTickets", booking.numberOfTickets)
            }
            startActivity(intent)
        }
        binding.myRv.adapter = adapter
    }
}