package com.robertojr.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.robertojr.moov.databinding.ActivityHistoricoBinding
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.ui.adapter.AdapterHistoricoCustomer
import com.robertojr.ui.adapter.AdapterHistoricoDriver
import com.robertojr.util.userSection
import kotlinx.coroutines.launch

class HistoricoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoricoBinding
    private lateinit var adapterHistoricoDriver: AdapterHistoricoDriver
    private lateinit var adapterHistoricoCustomer: AdapterHistoricoCustomer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val recicleViewHistorico = binding.rvHistorico

        recicleViewHistorico.layoutManager = LinearLayoutManager(this)
        recicleViewHistorico.setHasFixedSize(true)

        lifecycleScope.launch {


            when (userSection.type) {

                "DRIVER" -> {

                    val response = RetrofitClient.historicoRetrofit.findByDriver(userSection.id)
                    Log.d("Debug",response.body().toString())
                    Log.d("Debug",response.errorBody().toString())
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.GONE
                        val lista = response.body() ?: emptyList()
                        if (lista.isNotEmpty()) {
                            recicleViewHistorico.visibility = View.VISIBLE
                            adapterHistoricoDriver =
                                AdapterHistoricoDriver(this@HistoricoActivity, lista)
                            recicleViewHistorico.adapter = adapterHistoricoDriver
                        } else {
                            binding.imgSemHistorico.visibility = View.VISIBLE
                            binding.txtSemHistorico.visibility = View.VISIBLE
                        }
                    } else {
                        binding.imgSemHistorico.visibility = View.VISIBLE
                        binding.txtSemHistorico.text = "Falha ao carregar histórico, tente novamente mais tarde"
                        binding.txtSemHistorico.visibility = View.VISIBLE

                    }



                }

                "CUSTOMER" -> {

                    val response = RetrofitClient.historicoRetrofit.findByCustomer(userSection.id)
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.GONE
                        val lista = response.body() ?: emptyList()
                        if (lista.isNotEmpty()) {
                            recicleViewHistorico.visibility = View.VISIBLE
                            adapterHistoricoCustomer =
                                AdapterHistoricoCustomer(this@HistoricoActivity, lista)
                            recicleViewHistorico.adapter = adapterHistoricoCustomer
                        } else {
                            binding.imgSemHistorico.visibility = View.VISIBLE
                            binding.txtSemHistorico.visibility = View.VISIBLE
                        }
                    } else {
                        binding.imgSemHistorico.visibility = View.VISIBLE
                        binding.txtSemHistorico.text = "Falha ao carregar histórico, tente novamente mais tarde"
                        binding.txtSemHistorico.visibility = View.VISIBLE

                    }

                }
            }


        }
        binding.btnVoltar.setOnClickListener {
            finish()
        }

    }



}
