package com.example.gradfront

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.gradfront.data.SongRecommendResponse
import com.example.gradfront.databinding.ActivityMainBinding
import com.example.gradfront.fragment.MainFragment
import com.example.gradfront.fragment.PerformListFragment
import com.example.gradfront.fragment.SongFragment1
import com.example.gradfront.fragment.SongFragment2

class MainActivity : AppCompatActivity() {
    var waitTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 사용자 ID 불러오기
        val userId = getUserId(applicationContext)
        Log.d("MainActivity", "불러온 사용자 ID: $userId")

        //내비게이션 바 코드
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, MainFragment())
            .commitAllowingStateLoss()
        binding.mainBtmNav.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.perf -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, PerformListFragment())
                            .commit()
                    }

                    R.id.song -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, SongFragment1())
                            .commit()
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }

        binding.mainBtn.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, MainFragment())
                .commit()
            // BottomNavigationView의 선택 상태 초기화
            binding.mainBtmNav.menu.findItem(R.id.home).isChecked = true  // home으로 이동 시 선택 상태를 초기화
        }

        // 데이터 수신
        val artistName = intent.getStringExtra("artistName")
        val trackList: ArrayList<SongRecommendResponse>? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    "trackList",
                    ArrayList::class.java
                ) as? ArrayList<SongRecommendResponse>
            } else {
                intent.getSerializableExtra("trackList") as? ArrayList<SongRecommendResponse>
            }

        if (trackList != null) {
            // Fragment에 데이터 전달
            val fragment = SongFragment2().apply {
                arguments = Bundle().apply {
                    putString("artistName", artistName)
                    putSerializable("trackList", trackList)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, fragment)
                .commit()
        }

        binding.MyPageBtn.setOnClickListener {
            val intent = Intent(this, MyPage::class.java)
            startActivity(intent)
        }
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

    override fun onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
        } else {
            finish() // 액티비티 종료
        }
    }
}
