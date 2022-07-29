package com.example.moviesearch.ui.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesearch.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private val binding: ActivityWebBinding by lazy {
        ActivityWebBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}