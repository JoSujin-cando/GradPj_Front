package com.example.gradfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.gradfront.databinding.ActivityBookingPageBinding

class BookingPage : AppCompatActivity() {
    lateinit var binding: ActivityBookingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 예약 정보와 라이브 정보 표시(intent로부터 데이터 받기)
        val title = intent.getStringExtra("title")
        val bookingDate = intent.getStringExtra("bookingDate")
        val date = intent.getStringExtra("date")
        val place = intent.getStringExtra("place")
        val timetable = intent.getStringExtra("timetable")
        val notice = intent.getStringExtra("notice")
        val totalAmount = intent.getIntExtra("totalAmount", 0)
        val numberOfTickets = intent.getIntExtra("numberOfTickets", 0)
        val imageResId = intent.getStringExtra("imageResId")

        //받은 데이터를 UI에 표시
        binding.bookTitle.text = "공연명: $title"
        binding.bookDate.text = "예매 일시: $bookingDate"
        binding.bookLiveDate.text = "공연일시: $date"
        binding.bookPlace.text = "공연장소: $place"
        binding.bookPrice.text = "예매가: $totalAmount ($numberOfTickets)"
        binding.bookTimeTable.text = "$timetable"
        binding.bookNotice.text = "$notice"
        binding.imageView.load(imageResId)
    }
}