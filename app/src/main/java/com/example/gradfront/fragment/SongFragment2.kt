package com.example.gradfront.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
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

    }

    private fun setupRecyclerView() {
        binding.song2Rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}