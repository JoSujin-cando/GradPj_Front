package com.example.gradfront.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gradfront.SpacingItem
import com.example.gradfront.databinding.FragmentSong1Binding
import com.example.gradfront.databinding.FragmentSong2Binding


class SongFragment2 : Fragment() {
    lateinit var binding: FragmentSong2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSong2Binding.inflate(layoutInflater, container, false)

        //itemDecoration
        binding.song2Rv.addItemDecoration(SpacingItem(20))

        return binding.root
    }

}