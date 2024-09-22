package com.example.gradfront

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.gradfront.data.BookingRequest
import com.example.gradfront.data.PayRequest
import com.example.gradfront.databinding.ActivityPerformList2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformList2 : AppCompatActivity() {
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPerformList2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 데이터 받기
        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.ic_launcher_background)

        // 받은 데이터를 UI에 표시
        binding.PerfTitle.text = "공연명: $title"
        binding.PerfPlace.text = "공연일시: $subtitle"
        binding.imageView2.setImageResource(imageResId)

        //인원 수 버튼 클릭 시
        binding.minusBtn.setOnClickListener {
            if (count != 0)
                count--
            binding.num.setText(count.toString())
        }
        binding.plusBtn.setOnClickListener {
            count++
            binding.num.setText(count.toString())
        }
        //결제하기 클릭 시 결제 서비스로 연결
        binding.payBtn.setOnClickListener {
            val userId : Long = 1
            val liveId = 1
            val ticketCount = 3

            prepareBookingAndPayment(userId, liveId, ticketCount)
        }
    }

    private fun prepareBookingAndPayment(userId: Long, liveId: Int, numberOfTickets: Int) {
        val apiService: ApiService = ApiClient.getApiService()
        CoroutineScope(Dispatchers.IO).launch {
            // 1. 예약 생성
            val bookingRequest = BookingRequest(userId, liveId, numberOfTickets)
            val bookingResponse = apiService.createBooking(bookingRequest)

            if (bookingResponse.isSuccessful) {
                val bookingId = bookingResponse.body()?.id
                Log.d("Booking", bookingId.toString())

                // 2. 결제 준비 요청
                if (bookingId != null) {
                    val paymentResponse = apiService.readyPayment(PayRequest(bookingId))

                    if (paymentResponse.isSuccessful) {
                        val kakaoPayResponse = paymentResponse.body()

                        // 3. Android App Scheme을 이용해 결제 페이지로 이동
                        kakaoPayResponse?.next_redirect_mobile_url?.let {
                            Log.d("KakaoPaying", it)
                            openWebPage(it)
                        }

                    }
                }
            }
        }
    }

    private fun openWebPage(url: String) {
        val newUrl = url.replace("localhost", "172.30.1.21")
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newUrl))
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("KakaoPay", "Cannot open web page: $newUrl", e)
        }
    }

}


