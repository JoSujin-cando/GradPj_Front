package com.example.gradfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load
import com.example.gradfront.data.BookingRequest
import com.example.gradfront.data.PayRequest
import com.example.gradfront.databinding.ActivityBookingPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingPage : AppCompatActivity() {
    lateinit var binding: ActivityBookingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 예약 정보와 라이브 정보 표시(intent로부터 데이터 받기)
        val bookingId = intent.getLongExtra("bookingId", 0)
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

        // 결제 취소
        binding.cancelBtn.setOnClickListener {
            val apiService = ApiClient.getApiService()

            // 코루틴을 직접 실행
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Retrofit을 사용하여 네트워크 요청
                    val payRequest = PayRequest(bookingId)
                    val cancelPayment = apiService.cancelPayment(payRequest)

                    if (cancelPayment.isSuccessful) {
                        // 응답이 성공한 경우
                        val cancelResponse = cancelPayment.body() // 성공한 응답 본문
                        cancelResponse?.let {
                            Log.d("PaymentCancel", "결제 취소 성공")
                            // 결제 취소가 성공하면 나의 페이지로 이동
                            withContext(Dispatchers.Main) {
                                // "나의 페이지"로 이동
                                val intent = Intent(this@BookingPage, MyPage::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()  // 현재 액티비티 종료
                            }
                        }
                    } else {
                        // 응답이 실패한 경우
                        Log.d("PaymentCancel", "결제 취소 실패")
                    }
                } catch (e: Exception) {
                    // 네트워크 요청 중 예외 발생 시
                    Log.e("PaymentCancel", "결제 취소 중 오류 발생", e)
                }
            }
        }
    }
}