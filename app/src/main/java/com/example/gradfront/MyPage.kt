package com.example.gradfront

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.gradfront.data.*
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
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    removeUserId(applicationContext)  // 로그아웃 성공 시 유저 ID 삭제

                    // 로그인 페이지로 이동
                    val intent = Intent(this, LoginPage::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티 종료
                }
            }
        }

        // RecyclerView 설정
        binding.myRv.layoutManager = LinearLayoutManager(this)
        binding.myRv.addItemDecoration(SpacingItem(20))

        // *Retrofit을 통해 데이터를 불러옴*
        fetchLiveData()
    }

    private fun fetchLiveData() {
        val userId = getUserId(applicationContext)
        ApiClient.getApiService().getUserBookings(userId).enqueue(object : Callback<List<BookingResponse>> {
            override fun onResponse(call: Call<List<BookingResponse>>, response: Response<List<BookingResponse>>) {
                if (response.isSuccessful) {
                    // COMPLETED 상태의 예매만 필터링
                    var bookingList = response.body()?.filter { it.status == BookingStatus.COMPLETED } ?: emptyList()
                    // liveDataList를 id 순으로 오름차순 정렬
                    bookingList = bookingList.sortedBy { it.id }
                    fetchLiveDataWithClubs(bookingList)
                } else {
                    showToast("Failed to load booking data")
                }
            }

            override fun onFailure(call: Call<List<BookingResponse>>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun fetchLiveDataWithClubs(bookingList: List<BookingResponse>) {
        val liveDataList = mutableListOf<Pair<BookingResponse, LiveDataWithClub?>>()
        var completedRequests = 0

        bookingList.forEach { booking ->
            ApiClient.getApiService().getLiveDataById(booking.liveId).enqueue(object : Callback<LiveData> {
                override fun onResponse(call: Call<LiveData>, liveResponse: Response<LiveData>) {
                    if (liveResponse.isSuccessful) {
                        Log.d("라이브데이터", liveResponse.body().toString())
                        liveResponse.body()?.let { liveData ->
                            fetchClubData(booking, liveData, liveDataList) {
                                completedRequests++
                                checkIfCompleted(completedRequests, bookingList.size, liveDataList)
                            }
                        } ?: handleFailedRequest(booking, liveDataList) { completedRequests++ }
                    } else {
                        Log.d("라이브데이터", "라이브 데이터 오류")
                        handleFailedRequest(booking, liveDataList) { completedRequests++ }
                    }
                }

                override fun onFailure(call: Call<LiveData>, t: Throwable) {
                    handleFailedRequest(booking, liveDataList) { completedRequests++ }
                }
            })
        }
    }

    private fun fetchClubData(booking: BookingResponse, liveData: LiveData, liveDataList: MutableList<Pair<BookingResponse, LiveDataWithClub?>>, onComplete: () -> Unit) {
        ApiClient.getApiService().getClubDataById(liveData.clubId).enqueue(object : Callback<ClubData> {
            override fun onResponse(call: Call<ClubData>, clubResponse: Response<ClubData>) {
                val clubName = if (clubResponse.isSuccessful) {
                    clubResponse.body()?.clubName ?: "Unknown Club"
                } else {
                    "Unknown Club"
                }
                liveDataList.add(Pair(booking, LiveDataWithClub(liveData, clubName)))
                onComplete()
            }

            override fun onFailure(call: Call<ClubData>, t: Throwable) {
                liveDataList.add(Pair(booking, LiveDataWithClub(liveData, "Unknown Club")))
                onComplete()
            }
        })
    }

    private fun handleFailedRequest(booking: BookingResponse, liveDataList: MutableList<Pair<BookingResponse, LiveDataWithClub?>>, onComplete: () -> Unit) {
        liveDataList.add(Pair(booking, null))
        onComplete()
    }

    private fun checkIfCompleted(completedRequests: Int, totalRequests: Int, liveDataList: List<Pair<BookingResponse, LiveDataWithClub?>>) {
        if (completedRequests == totalRequests) {
            setUpRecyclerView(liveDataList)
        }
    }

    private fun setUpRecyclerView(liveDataList: List<Pair<BookingResponse, LiveDataWithClub?>>) {
        val adapter = MyPageAdapter(liveDataList) { booking, live ->
            val intent = Intent(this@MyPage, BookingPage::class.java).apply {
                putExtra("bookingId", booking.id)
                putExtra("title", live?.liveData?.title)
                putExtra("bookingDate", booking.bookingDate)
                putExtra("date", live?.liveData?.date)
                putExtra("place", live?.liveData?.clubId)
                putExtra("totalAmount", booking.totalAmount)
                putExtra("timetable", live?.liveData?.timetable)
                putExtra("notice", live?.liveData?.notice)
                putExtra("imageResId", live?.liveData?.image)
                putExtra("numberOfTickets", booking.numberOfTickets)
                putExtra("time", live?.liveData?.startTime)
            }
            startActivity(intent)
        }
        binding.myRv.adapter = adapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MyPage, message, Toast.LENGTH_SHORT).show()
    }

    // 사용자 ID 불러오기 함수
    private fun getUserId(context: Context): Long {
        // 최신 MasterKey 생성
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // 암호화된 SharedPreferences 생성
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "encrypted_prefs", // 저장된 파일 이름
            masterKey, // 마스터 키
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // 저장된 사용자 ID를 불러옴 (없으면 기본값 0L 반환)
        return sharedPreferences.getLong("userId", 0L)
    }
}

    private fun removeUserId(context: Context) {
        // 최신 MasterKey 생성
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // 암호화된 SharedPreferences 생성
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "encrypted_prefs", // 저장된 파일 이름
            masterKey, // 마스터 키
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // "userId" 데이터 삭제
        val editor = sharedPreferences.edit()
        editor.remove("userId")
        editor.apply()

        // 삭제 후 "userId"가 존재하는지 확인
        val deletedUserId = sharedPreferences.getString("userId", null)

        if (deletedUserId == null) {
            Log.d(TAG, "userId가 성공적으로 삭제되었습니다.")
        } else {
            Log.d(TAG, "userId 삭제에 실패했습니다. 현재 값: $deletedUserId")
        }
    }

