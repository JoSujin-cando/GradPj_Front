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
import com.example.gradfront.data.LiveData
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

//        // 백엔드에서 마커 ID 리스트를 가져와서 버튼 색상 변경하기
//        val markerIdsFromBackend = getMarkerIdsFromBackend() // 백엔드에서 마커 ID를 받아오는 함수
//        changeMarkerColors(markerIdsFromBackend) // 마커 ID에 따라 버튼 색상 변경

        return binding.root
    }

    //리사이클러뷰 Adapter랑 layoutmanager 붙이기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        binding.mainRv.layoutManager = LinearLayoutManager(requireContext())

        /*Retrofit을 통해 데이터를 불러옴*/
        fetchLiveData()

        // MainAdapter에 클릭 리스너 설정
//        val adapter = MainAdapter(getData()) { item ->
//            // 아이템 클릭 시 PerformList2Activity로 데이터 전달
//            val intent = Intent(requireContext(), PerformList2::class.java).apply {
//                putExtra("title", item.title)
//                putExtra("subtitle", item.subtitle)
//                putExtra("imageResId", item.imageResId)
//            }
//            startActivity(intent)
//        }
//        binding.mainRv.adapter = adapter
    }

    /**/
    private fun fetchLiveData() {
        // API 호출
        ApiClient.getApiService().getLiveData().enqueue(object : Callback<List<LiveData>> {
            override fun onResponse(call: Call<List<LiveData>>, response: Response<List<LiveData>>) {
                if (response.isSuccessful) {
                    val liveDataList = response.body() ?: emptyList()

                    // Adapter에 데이터를 전달하여 RecyclerView에 표시
                    val adapter = MainAdapter(liveDataList) { item ->
                        // 아이템 클릭 시 PerformList2Activity로 데이터 전달
                        val intent = Intent(requireContext(), PerformList2::class.java).apply {
                            putExtra("title", item.title)
                            putExtra("subtitle", item.bandLineup)
                            putExtra("date", item.date)
                            putExtra("place", item.club_id)
                            putExtra("genre", item.genre)
                            putExtra("price", item.advancePrice)
                            putExtra("timetable", item.timetable)
                            putExtra("notice", item.notice)
                            putExtra("imageResId", item.image) // 이미지 URL 전달-수정해야 함
                        }
                        startActivity(intent)
                    }
                    binding.mainRv.adapter = adapter

                    // club_id를 기반으로 마커 색상을 변경
                    val markerIds = liveDataList.map { liveData -> liveData.club_id }
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

//    private fun getData(): List<ItemData> {
//        return listOf(
//            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
//            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
//            ItemData(R.drawable.song, "Club CC", "시루봉"),
//            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
//            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
//            ItemData(R.drawable.song, "Club CC", "시루봉"),
//            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
//            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
//            ItemData(R.drawable.song, "Club CC", "시루봉")
//        )
//    }

    // 현재 날짜를 가져오는 함수
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    // 백엔드에서 마커 ID를 가져오는 함수 (예시로 임의의 ID 리스트 반환)
//    private fun get
//    MarkerIdsFromBackend(): List<Int> {
//        // 실제로는 백엔드와 통신하여 마커 ID 리스트를 받아오는 로직이 필요함
//        // 예시로 마커 ID가 1, 3일 경우를 가정
//        return listOf(1, 3)
//    }

    // 마커 ID에 따라 해당하는 버튼의 색상 변경
    private fun changeMarkerColors(markerIds: List<String>) {
        // 모든 마커의 색상을 기본값으로 설정 (원하는 기본 색상 지정)
        binding.marker1.setBackgroundColor(Color.GRAY)
        binding.marker2.setBackgroundColor(Color.GRAY)
        binding.marker3.setBackgroundColor(Color.GRAY)
        binding.marker4.setBackgroundColor(Color.GRAY)
        binding.marker5.setBackgroundColor(Color.GRAY)

        // 마커 ID에 따라 해당하는 버튼의 색상을 변경
        for (markerId in markerIds) {
            when (markerId) {
                "Club FF" -> binding.marker1.setColorFilter(Color.RED)
                "CLUB A.O.R" -> binding.marker2.setColorFilter(Color.RED)
                "언플러그드" -> binding.marker3.setColorFilter(Color.RED)
                "클럽빵" -> binding.marker4.setColorFilter(Color.RED)
                "CLUB VICTIM" -> binding.marker5.setColorFilter(Color.RED)
            }
        }
    }
}