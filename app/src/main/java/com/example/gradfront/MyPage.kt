package com.example.gradfront

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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

        //itemDecoration
        binding.myRv.addItemDecoration(SpacingItem(20))
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