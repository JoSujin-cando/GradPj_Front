package com.example.gradfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.gradfront.data.SongRecommendResponse
import com.example.gradfront.databinding.ActivityMainBinding
import com.example.gradfront.fragment.MainFragment
import com.example.gradfront.fragment.PerformListFragment
import com.example.gradfront.fragment.SongFragment1
import com.example.gradfront.fragment.SongFragment2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //내비게이션 바 코드
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, MainFragment()).commitAllowingStateLoss()
        binding.mainBtmNav.run{
            setOnItemSelectedListener { item->
                when(item.itemId){
                    R.id.perf->{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, PerformListFragment())
                            .commit()
                    }
                    R.id.song->{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, SongFragment1())
                            .commit()
                    }
                }
                true
            }
            //selectedItemId = R.id.home
        }

        // 데이터 수신
        val trackList = intent.getSerializableExtra("trackList") as? ArrayList<SongRecommendResponse>

        if (trackList != null) {
            // Fragment에 데이터 전달
            val fragment = SongFragment2().apply {
                arguments = Bundle().apply {
                    putSerializable("trackList", trackList)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, fragment)
                .commit()
        }

        binding.mainBtn.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, MainFragment())
                .commit()
        }

        binding.MyPageBtn.setOnClickListener {
            val intent = Intent(this, MyPage::class.java)
            startActivity(intent)
        }
    }

}