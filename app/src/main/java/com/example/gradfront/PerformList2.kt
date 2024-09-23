package com.example.gradfront

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.gradfront.data.BookingRequest
import com.example.gradfront.data.PayRequest
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import coil.load
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
        val date = intent.getStringExtra("date")
        val place = intent.getStringExtra("place")
        val genre = intent.getStringExtra("genre")
        val price = intent.getIntExtra("price",0)
        val timetable = intent.getStringExtra("timetable")
        val notice = intent.getStringExtra("notice")
        val imageResId = intent.getStringExtra("imageResId") //Img 수정해야 함
        val check = intent.getIntExtra("check", 0)
        val liveId = intent.getLongExtra("liveId", 1)

        // 받은 데이터를 UI에 표시
        binding.PerfTitle.text = "공연명: $title"
        binding.PerfDate.text = "공연일시: $date"
        binding.PerfPlace.text = "공연장소: $place"
        binding.genre.text = "장르: $genre"
        binding.price.text = "예매가: $price"
        binding.TimeTable.text = "$timetable"
        binding.notice.text = "$notice"
        binding.imageView2.load(imageResId)

        //인원 수 버튼 클릭 시
        binding.minusBtn.setOnClickListener {
            if (check == 1) {
                binding.minusBtn.isEnabled = false
                Toast.makeText(this, "이미 지난 공연입니다", Toast.LENGTH_SHORT).show()
            } else {
                if (count != 0)
                    count--
                binding.num.setText(count.toString())
            }
        }

        binding.plusBtn.setOnClickListener {
            if (check == 1) {
                binding.plusBtn.isEnabled = false
                Toast.makeText(this, "이미 지난 공연입니다", Toast.LENGTH_SHORT).show()
            } else {
                count++
                binding.num.setText(count.toString())
            }
        }

        //결제하기 클릭 시 결제 서비스로 연결
        binding.payBtn.setOnClickListener {
            if (check == 1) {
                //버튼 클릭 시 결제 서빅스로 연결
                val userId = getUserId(applicationContext)
                val liveId = 1
                val ticketCount = count
                prepareBookingAndPayment(userId, liveId, ticketCount)
            } else {
                binding.payBtn.isEnabled = false
                Toast.makeText(this, "이미 지난 공연입니다", Toast.LENGTH_SHORT).show()
            }
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
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("KakaoPay", "Cannot open web page: $url", e)
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
}



