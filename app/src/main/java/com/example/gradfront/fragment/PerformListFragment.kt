package com.example.gradfront.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.gradfront.databinding.FragmentPerformListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PerformListFragment : Fragment() {
    lateinit var binding: FragmentPerformListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerformListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        binding.perfRv.layoutManager = LinearLayoutManager(requireContext())

        // *Retrofit을 통해 데이터를 불러옴*
        fetchLiveData()

        //itemDecoration
        binding.perfRv.addItemDecoration(SpacingItem(20))
    }

    override fun onResume() {
        super.onResume()
        // PerformList2Activity에서 결제가 완료된 후 다시 돌아왔을 때 데이터를 새로고침
        fetchLiveData()
    }

    /**/
    private fun fetchLiveData() {
        // API 호출
        ApiClient.getApiService().getAllData().enqueue(object : Callback<List<LiveData>> {
            override fun onResponse(
                call: Call<List<LiveData>>,
                response: Response<List<LiveData>>
            ) {
                if (response.isSuccessful) {
                    var liveDataList = response.body() ?: emptyList()

                    // 클럽 데이터를 각 라이브에 맞춰 가져오고 리사이클러뷰에 전달
                    val updatedLiveDataList = mutableListOf<LiveDataWithClub>()

                    liveDataList.forEach { liveData ->
                        ApiClient.getApiService().getClubDataById(liveData.clubId).enqueue(object : Callback<ClubData> {
                            override fun onResponse(call: Call<ClubData>, clubResponse: Response<ClubData>) {
                                if (clubResponse.isSuccessful) {
                                    val club = clubResponse.body()
                                    Log.d("공연리스트", club.toString())
                                    if (club != null) {
                                        // 클럽 데이터를 포함하는 새로운 데이터 클래스를 사용
                                        updatedLiveDataList.add(LiveDataWithClub(liveData, club.clubName))
                                        if (updatedLiveDataList.size == liveDataList.size) {
                                            // 모든 데이터를 다 가져온 후에 다시 정렬
                                            updatedLiveDataList.sortBy { it.liveData.id }
                                            // Adapter 설정
                                            setupRecyclerView(updatedLiveDataList)
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<ClubData>, t: Throwable) {
                                // 클럽 데이터 가져오기 실패 처리
                            }
                        })
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<LiveData>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(liveDataList: List<LiveDataWithClub>) {
        val adapter = PerformListAdapter(liveDataList) { item ->
            // PerformList2Activity로 이동할 때 클럽 이름도 함께 전달
            val intent = Intent(requireContext(), PerformList2::class.java).apply {
                if (getCurrentDate().compareTo(item.liveData.date,true)>0) { //이미 지난 날짜인 경우(왼쪽이 크면 1, 작으면 -1)
                    putExtra("check", 1)
                }else{
                    putExtra("check", 0)
                }
                putExtra("title", item.liveData.title)
                putExtra("subtitle", item.liveData.bandLineup)
                putExtra("date", item.liveData.date)
                putExtra("place", item.clubName)
                putExtra("genre", item.liveData.genre)
                val price = item.liveData.advancePrice ?: 0.0
                putExtra("price", price)
                putExtra("timetable", item.liveData.timetable)
                putExtra("notice", item.liveData.notice)
                putExtra("imageResId", item.liveData.image) // 이미지 URL 전달-수정해야 함
                putExtra("liveId", item.liveData.id)
                putExtra("seat", item.liveData.remainNumOfSeats)
                putExtra("time", item.liveData.startTime)
            }
            startActivity(intent)
        }
        binding.perfRv.adapter = adapter
    }

    /*onBindViewHolder의 if 문을 위해 MainFragment에서 가져옴: 현재 날짜를 가져오는 함수*/
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}