package com.example.gradfront.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gradfront.databinding.FragmentMainBinding
import com.example.gradfront.databinding.FragmentSong1Binding

class SongFragment1 : Fragment() {
    lateinit var binding : FragmentSong1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSong1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }
}