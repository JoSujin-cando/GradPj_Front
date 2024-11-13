package com.example.gradfront.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradfront.*
import com.example.gradfront.data.LiveData
import com.example.gradfront.data.LiveDataWithClub
import com.example.gradfront.data.UserClubResponse
import com.example.gradfront.databinding.FragmentMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentMainBinding
    private lateinit var mMap: GoogleMap
    private val apiKey = "AIzaSyAIFUbIPkl4_6HV2dZQmL3VACMCMD4vFks" // Geocoding API 키 입력

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        // 현재 날짜를 TextView에 설정
        val currentDate = getCurrentDateToText()
        binding.date.text = currentDate

        // SupportMapFragment를 초기화하고 지도가 준비되면 onMapReady를 호출
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainRv.layoutManager = LinearLayoutManager(requireContext())
        fetchLiveData() // 데이터 로드
    }

    override fun onResume() {
        super.onResume()
        fetchLiveData() // 돌아올 때마다 데이터를 갱신
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fetchLiveData() // 지도 준비 후 데이터 로드
    }

    private fun fetchLiveData() {
        ApiClient.getApiService().getLiveData().enqueue(object : Callback<List<LiveData>> {
            override fun onResponse(call: Call<List<LiveData>>, response: Response<List<LiveData>>) {
                if (response.isSuccessful) {
                    val liveDataList = response.body() ?: emptyList()
                    val updatedLiveDataList = mutableListOf<LiveDataWithClub>()

                    liveDataList.forEach { liveData ->
                        ApiClient.getApiService().getClubDataById(liveData.clubId).enqueue(object : Callback<UserClubResponse> {
                            override fun onResponse(call: Call<UserClubResponse>, clubResponse: Response<UserClubResponse>) {
                                if (clubResponse.isSuccessful) {
                                    val club = clubResponse.body()
                                    if (club != null) {
                                        // 클럽 정보와 함께 LiveDataWithClub 객체 생성
                                        updatedLiveDataList.add(
                                            LiveDataWithClub(
                                                liveData = liveData,
                                                clubName = club.clubName,
                                                location = club.location // 클럽 위치 추가
                                            )
                                        )
                                        if (updatedLiveDataList.size == liveDataList.size) {
                                            updatedLiveDataList.sortBy { it.liveData.id }
                                            setupRecyclerView(updatedLiveDataList)
                                            addMarkersToMap(updatedLiveDataList) // 위치 정보 기반 마커 추가
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<UserClubResponse>, t: Throwable) {
                                Log.e("MainFragment", "Failed to fetch club info", t)
                            }
                        })
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<LiveData>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(liveDataList: List<LiveDataWithClub>) {
        val adapter = MainAdapter(liveDataList) { item ->
            val intent = Intent(requireContext(), PerformList2::class.java).apply {
                putExtra("title", item.liveData.title)
                putExtra("subtitle", item.liveData.bandLineup)
                putExtra("date", item.liveData.date)
                putExtra("place", item.clubName)
                putExtra("location", item.location)
                putExtra("genre", item.liveData.genre)
                putExtra("price", item.liveData.advancePrice)
                putExtra("timetable", item.liveData.timetable)
                putExtra("notice", item.liveData.notice)
                putExtra("imageResId", item.liveData.image)
                putExtra("liveId", item.liveData.id)
                putExtra("seat", item.liveData.remainNumOfSeats)
                putExtra("time", item.liveData.startTime)
            }
            startActivity(intent)
        }
        binding.mainRv.adapter = adapter
    }

    private fun getCurrentDateToText(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    // 날짜 비교 함수: 공연이 오늘인지 확인
    private fun isEventToday(eventDate: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val event = dateFormat.parse(eventDate)
        val today = dateFormat.parse(getCurrentDateToText())

        return event != null && today != null && event == today
    }

    private fun addMarkersToMap(liveDataList: List<LiveDataWithClub>) {
        for (liveDataWithClub in liveDataList) {
            geocodeAddressToLatLng(liveDataWithClub.location) { latLng ->
                val markerColor = if (isEventToday(liveDataWithClub.liveData.date)) {
                    BitmapDescriptorFactory.HUE_RED // 오늘 공연이 있으면 빨간색
                } else {
                    BitmapDescriptorFactory.HUE_GREEN // 오늘 공연이 없으면 회색
                }

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("${liveDataWithClub.liveData.title} - ${liveDataWithClub.clubName}")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor))

                activity?.runOnUiThread {
                    mMap.addMarker(markerOptions)
                    if (liveDataList.indexOf(liveDataWithClub) == 0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    }
                }
            }
        }
    }

    private fun geocodeAddressToLatLng(address: String, callback: (LatLng) -> Unit) {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${address}&key=${apiKey}"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val json = response.body?.string()
                if (response.isSuccessful && json != null) {
                    val jsonObject = JSONObject(json)
                    val results = jsonObject.getJSONArray("results")
                    if (results.length() > 0) {
                        val location = results.getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location")
                        val lat = location.getDouble("lat")
                        val lng = location.getDouble("lng")
                        callback(LatLng(lat, lng))
                    }
                } else {
                    Log.e("Geocoding", "Geocoding API failed: ${response.message}")
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("Geocoding", "Request failed: ${e.message}")
            }
        })
    }
}
