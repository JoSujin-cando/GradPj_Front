package com.example.gradfront.fragment

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gradfront.MainActivity
import com.example.gradfront.R
import com.example.gradfront.SongAdapter
import com.example.gradfront.data.SongRecommendResponse

import com.example.gradfront.SpacingItem
import com.example.gradfront.databinding.FragmentSong1Binding

import com.example.gradfront.databinding.FragmentSong2Binding


class SongFragment2 : Fragment() {
    lateinit var binding: FragmentSong2Binding
    private lateinit var adapter: SongAdapter
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSong2Binding.inflate(layoutInflater, container, false)

        //itemDecoration
        binding.song2Rv.addItemDecoration(SpacingItem(20))

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView와 Adapter 설정
        setupRecyclerView()

        val artistName = arguments?.getString("artistName")
        val trackList = arguments?.getSerializable("trackList") as? ArrayList<SongRecommendResponse>

        if (trackList != null) {
            binding.textView13.text = "$artistName 와(과) 비슷한 밴드의 노래 추천 결과입니다"
            // Adapter에 트랙 리스트와 현재 Fragment 전달
            adapter = SongAdapter(trackList, this) // 변경된 부분
            binding.song2Rv.adapter = adapter
        }
        // 뒤로가기 버튼 동작 처리
        handleBackPressed()
    }

    fun playPreview(previewUrl: String) {
        // MediaPlayer가 이미 실행 중이라면 해제하고 새로 시작
        mediaPlayer?.release()

        // 새로운 MediaPlayer 인스턴스를 생성하고 미리듣기 URL 설정
        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl) // 미리듣기 URL 설정
            prepareAsync() // 비동기 준비
            setOnPreparedListener {
                start() // 준비가 완료되면 재생 시작
            }
            setOnCompletionListener {
                release() // 재생이 완료되면 리소스 해제
            }
            setOnErrorListener { mp, what, extra ->
                // 오류 처리
                Toast.makeText(requireContext(), "Error playing audio", Toast.LENGTH_SHORT).show()
                release() // 오류 발생 시 리소스 해제
                true // 오류 처리 완료
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Fragment가 화면에서 사라질 때 미디어 재생 중지 및 해제
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun setupRecyclerView() {
        binding.song2Rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun handleBackPressed() {
        // SongFragment2에서 뒤로가기 버튼을 누르면 SongFragment1으로 돌아가게 설정
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 동작 처리: SongFragment1으로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, SongFragment1())
                    .commit()

                // MainActivity의 currentFragmentTag를 갱신
                (activity as? MainActivity)?.updateCurrentFragmentTag("SongFragment1")
                remove() // 다시 뒤로가기 눌렀을 때는 해당 콜백이 동작하지 않도록 해제
            }
        })
    }
}