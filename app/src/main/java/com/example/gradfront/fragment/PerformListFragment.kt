package com.example.gradfront.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradfront.*
import com.example.gradfront.data.LiveData
import com.example.gradfront.databinding.FragmentPerformListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

//        // MainAdapter에 클릭 리스너 설정
//        val adapter = PerformListAdapter(getData()) { item ->
//            // 아이템 클릭 시 PerformList2Activity로 데이터 전달
//            val intent = Intent(requireContext(), PerformList2::class.java).apply {
//                putExtra("title", item.title)
//                putExtra("subtitle", item.subtitle)
//                putExtra("imageResId", item.imageResId)
//            }
//            startActivity(intent)
//        }
//        binding.perfRv.adapter = adapter

        //itemDecoration
        binding.perfRv.addItemDecoration(SpacingItem(20))
    }

    /**/
    private fun fetchLiveData() {
        // API 호출
        ApiClient.getApiService().getLiveData().enqueue(object : Callback<List<LiveData>> {
            override fun onResponse(
                call: Call<List<LiveData>>,
                response: Response<List<LiveData>>
            ) {
                if (response.isSuccessful) {
                    val liveDataList = response.body() ?: emptyList()

                    // Adapter에 데이터를 전달하여 RecyclerView에 표시
                    val adapter = PerformListAdapter(liveDataList) { item ->
                        // 아이템 클릭 시 PerformList2Activity로 데이터 전달
                        val intent = Intent(requireContext(), PerformList2::class.java).apply {
                            putExtra("title", item.title)
                            putExtra("subtitle", item.bandLineup)
                            putExtra("imageResId", item.image) // 이미지 URL 전달
                        }
                        startActivity(intent)
                    }
                    binding.perfRv.adapter = adapter
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

    private fun getData(): List<ItemData> {
        return listOf(
            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
            ItemData(R.drawable.song, "Club CC", "시루봉"),
            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
            ItemData(R.drawable.song, "Club CC", "시루봉"),
            ItemData(R.drawable.diskimg, "Club FF", "행로난"),
            ItemData(R.drawable.ic_baseline_account_circle_24, "Club BB", "몽롱이"),
            ItemData(R.drawable.song, "Club CC", "시루봉")
        )
    }
}