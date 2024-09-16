package com.example.gradfront

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gradfront.data.SongRecommendResponse

class SharedViewModel : ViewModel() {
    val trackData = MutableLiveData<List<SongRecommendResponse>>()

    fun setTrackData(data: List<SongRecommendResponse>) {
        trackData.value = data
    }
}