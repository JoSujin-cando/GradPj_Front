package com.example.gradfront

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradfront.databinding.ActivityMyPageBinding
import com.kakao.sdk.user.UserApiClient

class MyPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
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

        // MainAdapter에 클릭 리스너 설정
        val adapter = MyPageAdapter(getData()) { item ->
            // 아이템 클릭 시 BookingPage로 데이터 전달
            val intent = Intent(this, BookingPage::class.java).apply {
                putExtra("title", item.title)
                putExtra("subtitle", item.subtitle)
                putExtra("imageResId", item.imageResId)
            }
            startActivity(intent)
        }
        binding.myRv.adapter = adapter
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