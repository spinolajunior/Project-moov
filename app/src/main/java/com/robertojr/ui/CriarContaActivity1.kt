package com.robertojr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.databinding.ActivityCriarConta1Binding
import kotlinx.coroutines.launch

class CriarContaActivity1 : AppCompatActivity() {
    lateinit var binding: ActivityCriarConta1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarConta1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        lifecycleScope.launch {


        }
    }
}