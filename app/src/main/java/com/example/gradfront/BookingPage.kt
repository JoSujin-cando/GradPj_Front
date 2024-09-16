package com.example.gradfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gradfront.databinding.ActivityBookingPageBinding

class BookingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBookingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}