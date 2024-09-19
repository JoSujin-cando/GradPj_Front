package com.example.gradfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gradfront.databinding.ActivityRegisterPageBinding

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}