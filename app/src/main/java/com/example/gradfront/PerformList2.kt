package com.example.gradfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.gradfront.databinding.ActivityPerformList2Binding

class PerformList2 : AppCompatActivity() {
    private lateinit var binding: ActivityPerformList2Binding
    private lateinit var viewModel: MainViewModel
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformList2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // **MainViewModel 초기화**
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Intent로부터 데이터 받기
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val club_id = intent.getStringExtra("club_id")
        val genre = intent.getStringExtra("genre")
        val advanceprice = intent.getStringExtra("advancePrice")
        val notice = intent.getStringExtra("notice")
        val timetable = intent.getStringExtra("timetable")
        val imageResId = intent.getIntExtra("imageUrl", R.drawable.ic_launcher_background)

        // 받은 데이터를 UI에 표시
        binding.PerfTitle.text = "공연명: $title"
        binding.PerfDate.text = "공연일시: $date"
        binding.PerfPlace.text = "공연장소: $club_id"
        binding.genre.text = "장르: $genre"
        binding.price.text = "예매가: $advanceprice"
        binding.notice.text = "$notice"
        binding.TimeTable.text = "$timetable"
        binding.imageView2.setImageResource(imageResId)

        //인원 수 버튼 클릭 시
        binding.minusBtn.setOnClickListener {
            if(count!=0)
                count--
            binding.num.setText(count.toString())
        }
        binding.plusBtn.setOnClickListener {
            count++
            binding.num.setText(count.toString())
        }
        //결제하기 클릭 시 결제 서비스로 연결
        binding.payBtn.setOnClickListener {

        }
    }
}