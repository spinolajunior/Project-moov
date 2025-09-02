package com.robertojr.ui

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.robertojr.moov.databinding.ActivityCadastrarReservaBinding
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.moov.model.sending.Customer
import com.robertojr.moov.model.sending.Racer
import com.robertojr.moov.model.sending.ReserveSending
import com.robertojr.util.userSection
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Locale

class CadastrarReservaActivity : AppCompatActivity() {

    lateinit var binding: ActivityCadastrarReservaBinding
    private var vagasDisponiveis: Int = 0
    private var valorPorVaga: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarReservaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idCorrida: Long = intent.extras?.getLong("id") ?: 0L
        val origemBinding = binding.txtOrigem
        val destinoBinding = binding.txtDestino
        val valorVagaBinding = binding.txtValorVaga
        val vagasDispBinding = binding.txtVagasDisponiveis
        val descriptionBinding = binding.txtDescricao
        val valorTotalBinding = binding.txtValorTotal

        // Carrega dados da corrida
        carregarDadosCorrida(idCorrida, origemBinding, descriptionBinding, destinoBinding, valorVagaBinding, vagasDispBinding)

        // Atualiza o valor total conforme o usuário digita, limitando a quantidade
        binding.edtVagasDesejadas.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val qtd = s.toString().toIntOrNull() ?: 0
                val finalQtd = when {
                    qtd < 1 -> 0
                    qtd > vagasDisponiveis -> vagasDisponiveis
                    else -> qtd
                }
                if (finalQtd != qtd) {
                    binding.edtVagasDesejadas.setText(finalQtd.toString())
                    binding.edtVagasDesejadas.setSelection(finalQtd.toString().length)
                }
                val total = valorPorVaga * finalQtd
                valorTotalBinding.text = String.format(Locale.US, "R$ %.2f", total)
            }
        })

        // Botão de reservar
        binding.btnReservar.setOnClickListener {
            val reservas = binding.edtVagasDesejadas.text.toString().toIntOrNull() ?: 0
            if (reservas < 1) {
                Toast.makeText(this, "Informe a quantidade de vagas!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val envio = ReserveSending(
                    Customer(userSection.id),
                    Instant.now().toString(),
                    Racer(idCorrida),
                    reservas
                )
                Log.d("Debug", envio.toString())
                val requisicao = RetrofitClient.reserveApi.insert(envio)

                if (requisicao.isSuccessful) {
                    carregarDadosCorrida(idCorrida, origemBinding, descriptionBinding, destinoBinding, valorVagaBinding, vagasDispBinding)
                    Toast.makeText(this@CadastrarReservaActivity, "Reserva efetuada com sucesso!", Toast.LENGTH_SHORT).show()
                    binding.edtVagasDesejadas.text.clear()
                    binding.txtValorTotal.text = String.format(Locale.US, "R$ %.2f", 0.0)
                } else {
                    Toast.makeText(this@CadastrarReservaActivity, "Falha, tente novamente mais tarde", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun carregarDadosCorrida(
        idCorrida: Long,
        origemBinding: TextView,
        descriptionBinding: TextView,
        destinoBinding: TextView,
        valorVagaBinding: TextView,
        vagasDispBinding: TextView
    ) {
        lifecycleScope.launch {
            val requisicao = RetrofitClient.racerRetrofit.findById(idCorrida)
            if (requisicao.isSuccessful) {
                val corrida = requisicao.body()
                origemBinding.text = corrida?.origin
                destinoBinding.text = corrida?.destiny
                valorPorVaga = corrida?.pricePerVacancy ?: 0.0
                valorVagaBinding.text = String.format(Locale.US, "R$ %.2f", valorPorVaga)
                vagasDisponiveis = corrida?.vacanciesAvalaible ?: 0
                vagasDispBinding.text = vagasDisponiveis.toString()
                descriptionBinding.text = corrida?.description
            } else {
                Toast.makeText(this@CadastrarReservaActivity, "Falha ao obter dados da corrida", Toast.LENGTH_LONG).show()
            }
        }
    }
}
