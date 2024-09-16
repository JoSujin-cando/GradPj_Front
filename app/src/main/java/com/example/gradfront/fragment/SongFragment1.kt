package com.example.gradfront.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.gradfront.ApiClient
import com.example.gradfront.ApiService
import com.example.gradfront.MainActivity
import com.example.gradfront.R
import com.example.gradfront.SharedViewModel
import com.example.gradfront.data.SongRecommendResponse
import com.example.gradfront.databinding.FragmentSong1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongFragment1 : Fragment() {
    lateinit var binding : FragmentSong1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSong1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button1: Button = view.findViewById(R.id.button1) // view에서 버튼 참조

        button1.setOnClickListener {
            val artistName = button1.text.toString() // 버튼의 텍스트를 artistName으로 사용
            sendArtistNameToServer(artistName)
        }
    }

    private fun sendArtistNameToServer(artistName: String) {
        val apiService : ApiService = ApiClient.getApiService()
        val call = apiService.getSongRecommendations(artistName)

        call.enqueue(object : Callback<List<SongRecommendResponse>> {
            override fun onResponse(call: Call<List<SongRecommendResponse>>, response: Response<List<SongRecommendResponse>>) {
                Log.d("Retrofit", "Response received: ${response.body()}")  // 응답 로그 출력
                if (response.isSuccessful) {
                    val recommendedTracks = response.body()
                    if (recommendedTracks != null) {
                        // 데이터를 Activity로 전달
                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            putExtra("trackList", ArrayList(recommendedTracks))
                        }
                        startActivity(intent)
                    }
                } else {
                    // 에러 처리
                    Log.e("Error", "Response was not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<SongRecommendResponse>>, t: Throwable) {
                // 네트워크 오류 또는 서버와의 통신 실패 처리
                Log.e("Failure", "Failed to fetch data: ${t.message}")
            }
        })
    }
}