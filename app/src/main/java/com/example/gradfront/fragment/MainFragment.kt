package com.example.gradfront.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradfront.*
import com.example.gradfront.data.ClubData
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.LiveDataWithClub
import com.example.gradfront.data.UserClubResponse
import com.example.gradfront.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*



class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        // 현재 날짜를 가져와서 TextView에 넣기
        val currentDate = getCurrentDate()
        binding.date.text = currentDate

        return binding.root
    }

    //리사이클러뷰 Adapter랑 layoutmanager 붙이기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        binding.mainRv.layoutManager = LinearLayoutManager(requireContext())

        /*Retrofit을 통해 데이터를 불러옴*/
        fetchLiveData()
    }

    override fun onResume() {
        super.onResume()
        // 홈 화면에 돌아올 때마다 데이터를 갱신하도록 API 호출
        fetchLiveData()
    }

    /**/
    private fun fetchLiveData() {
        // API 호출
        ApiClient.getApiService().getLiveData().enqueue(object : Callback<List<LiveData>> {
            override fun onResponse(call: Call<List<LiveData>>, response: Response<List<LiveData>>) {
                if (response.isSuccessful) {
                    val liveDataList = response.body() ?: emptyList()

                    // 클럽 데이터를 각 라이브에 맞춰 가져오고 리사이클러뷰에 전달
                    val updatedLiveDataList = mutableListOf<LiveDataWithClub>()

                    liveDataList.forEach { liveData ->
                        ApiClient.getApiService().getClubDataById(liveData.clubId).enqueue(object : Callback<UserClubResponse> {
                            override fun onResponse(call: Call<UserClubResponse>, clubResponse: Response<UserClubResponse>) {
                                if (clubResponse.isSuccessful) {
                                    val club = clubResponse.body()
                                    if (club != null) {
                                        // 클럽 데이터를 포함하는 새로운 데이터 클래스를 사용
                                        updatedLiveDataList.add(LiveDataWithClub(liveData, club.clubName))
                                        if (updatedLiveDataList.size == liveDataList.size) {
                                            // 모든 데이터를 불러왔으면 Adapter 설정
                                            setupRecyclerView(updatedLiveDataList)
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<UserClubResponse>, t: Throwable) {
                                // 클럽 데이터 가져오기 실패 처리
                            }
                        })
                    }

                    // club_id를 기반으로 마커 색상을 변경
                    val markerIds = liveDataList.map { liveData -> liveData.clubId }
                    changeMarkerColors(markerIds)

                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<LiveData>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(liveDataList: List<LiveDataWithClub>) {
        val adapter = MainAdapter(liveDataList) { item ->
            // PerformList2Activity로 이동할 때 클럽 이름도 함께 전달
            val intent = Intent(requireContext(), PerformList2::class.java).apply {
                putExtra("title", item.liveData.title)
                putExtra("subtitle", item.liveData.bandLineup)
                putExtra("date", item.liveData.date)
                putExtra("place", item.clubName)  // clubName 추가
                putExtra("genre", item.liveData.genre)
                putExtra("price", item.liveData.advancePrice)
                putExtra("timetable", item.liveData.timetable)
                putExtra("notice", item.liveData.notice)
                putExtra("imageResId", item.liveData.image) // 이미지 URL 전달
                putExtra("liveId", item.liveData.id)
                putExtra("seat", item.liveData.remainNumOfSeats)
            }
            startActivity(intent)
        }
        binding.mainRv.adapter = adapter
    }

    // 현재 날짜를 가져오는 함수
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    // 마커 ID에 따라 해당하는 버튼의 색상 변경
    private fun changeMarkerColors(markerIds: List<Long>) {
        // 모든 마커의 색상을 기본값으로 설정 (원하는 기본 색상 지정)
        binding.marker1.setBackgroundColor(Color.GRAY)
        binding.marker2.setBackgroundColor(Color.GRAY)
        binding.marker3.setBackgroundColor(Color.GRAY)
        binding.marker4.setBackgroundColor(Color.GRAY)
        binding.marker5.setBackgroundColor(Color.GRAY)

        // 마커 ID에 따라 해당하는 버튼의 색상을 변경
        for (markerId in markerIds) {
            when (markerId) {
                1L -> binding.marker1.setColorFilter(Color.RED)
                2L -> binding.marker2.setColorFilter(Color.RED)
                3L -> binding.marker3.setColorFilter(Color.RED)
                4L -> binding.marker4.setColorFilter(Color.RED)
                5L -> binding.marker5.setColorFilter(Color.RED)
            }
        }
    }
}