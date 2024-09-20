package com.example.gradfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.gradfront.databinding.ActivityPerformList2Binding

class PerformList2 : AppCompatActivity() {
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPerformList2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 데이터 받기
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val place = intent.getStringExtra("place")
        val genre = intent.getStringExtra("genre")
        val price = intent.getStringExtra("price")
        val timetable = intent.getStringExtra("timetable")
        val notice = intent.getStringExtra("notice")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.ic_launcher_background) //Img 수정해야 함

        // 받은 데이터를 UI에 표시
        binding.PerfTitle.text = "공연명: $title"
        binding.PerfDate.text = "공연일시: $date"
        binding.PerfPlace.text = "공연장소: $place"
        binding.genre.text = "장르: $genre"
        binding.price.text = "예매가: $price"
        binding.TimeTable.text = "$timetable"
        binding.notice.text = "$notice"
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