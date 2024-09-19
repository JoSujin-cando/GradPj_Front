package com.example.gradfront

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
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
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }

        // RecyclerView 설정
        binding.myRv.layoutManager = LinearLayoutManager(this)

        // *Retrofit을 통해 데이터를 불러옴*
        fetchLiveData()

//        // MainAdapter에 클릭 리스너 설정
//        val adapter = MyPageAdapter(getData()) { item ->
//            // 아이템 클릭 시 BookingPage로 데이터 전달
//            val intent = Intent(this, BookingPage::class.java).apply {
//                putExtra("title", item.title)
//                putExtra("subtitle", item.subtitle)
//                putExtra("imageResId", item.imageResId)
//            }
//            startActivity(intent)
//        }
//        binding.myRv.adapter = adapter

        //itemDecoration
        binding.myRv.addItemDecoration(SpacingItem(20))
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
                    val adapter = MyPageAdapter(liveDataList) { item ->
                        // 아이템 클릭 시 PerformList2Activity로 데이터 전달
                        val intent = Intent(this@MyPage, BookingPage::class.java).apply {
                            putExtra("title", item.title)
                            putExtra("subtitle", item.bandLineup)
                            putExtra("imageResId", item.image) // 이미지 URL 전달
                        }
                        startActivity(intent)
                    }
                    binding.myRv.adapter = adapter
                } else {
                    Toast.makeText(this@MyPage, "Failed to load data", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<LiveData>>, t: Throwable) {
                Toast.makeText(this@MyPage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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