package com.example.gradfront

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gradfront.data.LivePerformanceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _livePerformances = MutableLiveData<List<LivePerformanceResponse>>()
    val livePerformances: LiveData<List<LivePerformanceResponse>> get() = _livePerformances

    fun fetchLivePerformances() {
        val apiService = ApiClient.getApiService()
        apiService.getLivePerformances().enqueue(object : Callback<List<LivePerformanceResponse>> {
            override fun onResponse(
                call: Call<List<LivePerformanceResponse>>,
                response: Response<List<LivePerformanceResponse>>
            ) {
                if (response.isSuccessful) {
                    _livePerformances.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<LivePerformanceResponse>>, t: Throwable) {
                // 에러 처리
            }
        })
    }
}
