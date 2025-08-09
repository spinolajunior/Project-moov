package com.robertojr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robertojr.moov.databinding.ActivityOptionsBinding

class ConfigActivity : AppCompatActivity() {
    lateinit var binding: ActivityOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnVoltar.setOnClickListener {
            finish()
        }


    }
}