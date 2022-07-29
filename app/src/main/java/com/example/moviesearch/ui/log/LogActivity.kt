package com.example.moviesearch.ui.log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesearch.databinding.ActivityLogBinding

class LogActivity : AppCompatActivity() {

    private val binding: ActivityLogBinding by lazy {
        ActivityLogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}