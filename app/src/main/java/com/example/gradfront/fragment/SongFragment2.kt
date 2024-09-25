package com.example.gradfront.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            // Adapter에 트랙 리스트 전달
            SongAdapter(trackList).also { adapter = it }
            binding.song2Rv.adapter = adapter
        }
        // 뒤로가기 버튼 동작 처리
        handleBackPressed()
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