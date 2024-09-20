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
import com.example.gradfront.data.LiveData
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

    /**/
    private fun fetchLiveData() {
        // 유저 ID는 로그인된 사용자 정보에서 받아오도록 설정
        val userId: Long = 1 // 예시로 고정값, 실제로는 로그인된 사용자의 ID로 대체

        ApiClient.getApiService().getUserBookings(userId).enqueue(object : Callback<List<BookingResponse>> {
            override fun onResponse(call: Call<List<BookingResponse>>, response: Response<List<BookingResponse>>) {
                if (response.isSuccessful) {
                    val bookingList = response.body() ?: emptyList()
                    Log.d("mypage", bookingList.toString())

                    // 각 예약 정보에서 라이브 정보를 추가로 불러옴
                    val liveDataList = mutableListOf<Pair<BookingResponse, LiveData?>>()

                    bookingList.forEach { booking ->
                        ApiClient.getApiService().getLiveDataById(booking.liveId).enqueue(object : Callback<LiveData> {
                            override fun onResponse(call: Call<LiveData>, liveResponse: Response<LiveData>) {
                                if (liveResponse.isSuccessful) {
                                    val liveData = liveResponse.body()
                                    liveDataList.add(Pair(booking, liveData))

                                    // Adapter에 데이터를 전달하여 RecyclerView에 표시
                                    if (liveDataList.size == bookingList.size) {
                                        val adapter = MyPageAdapter(liveDataList) { booking, live ->
                                            val intent = Intent(this@MyPage, BookingPage::class.java).apply {
                                                putExtra("title", live?.title)
                                                putExtra("bookingDate", booking.bookingDate)
                                                putExtra("date", live?.date)
                                                putExtra("place", live?.club_id)
                                                putExtra("totalAmount", booking.totalAmount)
                                                putExtra("timetable", live?.timetable)
                                                putExtra("notice", live?.notice)
                                                putExtra("imageResId", live?.image)
                                                putExtra("numberOfTickets", booking.numberOfTickets)
                                            }
                                            startActivity(intent)
                                        }
                                        binding.myRv.adapter = adapter
                                    }
                                }
                            }

                            override fun onFailure(call: Call<LiveData>, t: Throwable) {
                                Toast.makeText(this@MyPage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
}