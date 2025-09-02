package com.robertojr.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.robertojr.moov.databinding.ActivityCorridasBinding
import com.robertojr.moov.model.Racer
import com.robertojr.moov.model.RetrofitClient
import com.robertojr.ui.adapter.AdapterReserva
import com.robertojr.ui.adapter.Reserva
import kotlinx.coroutines.launch
import java.util.Locale

class CorridasActivity : AppCompatActivity() {

    lateinit var binding: ActivityCorridasBinding
    lateinit var adapterReserva: AdapterReserva
    lateinit var corrida: Racer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorridasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val idCorrida: Long? = intent.extras?.getLong("id")
        if (idCorrida == null) {
            Toast.makeText(this, "ID da corrida não fornecido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val origemBinding = binding.txtOrigem
        val destinoBinding = binding.txtDestino
        val valorVagaBinding = binding.txtValorVaga
        val vagasDispBinding = binding.txtVagasDisponiveis
        val descriptionBinding = binding.txtDescricao

        val rv = binding.rvCorridas
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)

        adapterReserva = AdapterReserva(this, mutableListOf())
        rv.adapter = adapterReserva

        carregarDadosCorrida(
            idCorrida,
            origemBinding,
            descriptionBinding,
            destinoBinding,
            valorVagaBinding,
            vagasDispBinding
        )

        carregarReservas(idCorrida) { lista ->
            Log.d("DEBUG", "Atualizando adapter com ${lista.size} reservas")
            // Adiciona header visual
            val listaComHeader = mutableListOf<Reserva>()
            if (lista.isNotEmpty()) {
                listaComHeader.add(Reserva(cliente = "Reservas", contato = "", vagas = 0, valor = 0.0))
                listaComHeader.addAll(lista)
            }
            adapterReserva.atualizarLista(listaComHeader)
        }

        binding.btnFinalizar.setOnClickListener {
            lifecycleScope.launch {
                corrida.racerStatus = "COMPLETED"
                val requisicao = RetrofitClient.racerRetrofit.updateById(corrida, idCorrida)
                if (requisicao.isSuccessful) {
                    Toast.makeText(this@CorridasActivity, "Corrida finalizada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CorridasActivity, "Erro ao finalizar corrida", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnExcluir.setOnClickListener {
            lifecycleScope.launch {
                val requisicao = RetrofitClient.racerRetrofit.deleteById(idCorrida)
                if (requisicao.isSuccessful) {
                    Toast.makeText(this@CorridasActivity, "Corrida excluída", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CorridasActivity, "Erro ao excluir corrida", Toast.LENGTH_SHORT).show()
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
            Log.d("DEBUG", "Iniciando requisição de corrida ID: $idCorrida")
            val requisicao = RetrofitClient.racerRetrofit.findById(idCorrida)
            if (requisicao.isSuccessful) {
                val corridaRecebida = requisicao.body()
                if (corridaRecebida != null) {
                    corrida = corridaRecebida
                    Log.d("DEBUG", "Corrida recebida: $corrida")
                    origemBinding.text = corrida.origin
                    destinoBinding.text = corrida.destiny
                    valorVagaBinding.text = String.format(Locale.US, "R$ %.2f", corrida.pricePerVacancy)
                    vagasDispBinding.text = corrida.vacanciesAvalaible.toString()
                    descriptionBinding.text = corrida.description
                }
            } else {
                Log.e("DEBUG", "Falha ao obter corrida. Código: ${requisicao.code()}")
                Toast.makeText(this@CorridasActivity, "Falha ao obter dados da corrida", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun carregarReservas(idCorrida: Long, listaResultado: (MutableList<Reserva>) -> Unit) {
        lifecycleScope.launch {
            Log.d("DEBUG", "Iniciando requisição de reservas para corrida ID: $idCorrida")

            val solicitacaoCorrida = RetrofitClient.racerRetrofit.findById(idCorrida)
            if (!solicitacaoCorrida.isSuccessful) {
                Log.e("DEBUG", "Falha ao buscar corrida. Código: ${solicitacaoCorrida.code()}")
                listaResultado(mutableListOf())
                return@launch
            }

            val valorReserva = solicitacaoCorrida.body()?.pricePerVacancy ?: 0.0
            val listaId = solicitacaoCorrida.body()?.reservesId ?: emptyList()
            Log.d("DEBUG", "Lista de IDs de reservas recebida: $listaId")

            val listaReserve = mutableListOf<Reserva>()

            for (item in listaId) {
                Log.d("DEBUG", "Buscando reserva ID: $item")
                val reserva = Reserva()

                val solicitacaoReserve = RetrofitClient.reserveApi.findById(item)
                if (!solicitacaoReserve.isSuccessful) {
                    Log.e("DEBUG", "Falha ao buscar reserva ID $item")
                    continue
                }

                val reservaRequisicao = solicitacaoReserve.body()
                reserva.vagas = reservaRequisicao?.vacancy ?: 0

                val requisicaoCliente = RetrofitClient.customerRetrofit.findById(reservaRequisicao?.customerId ?: 0)
                val cliente = requisicaoCliente.body()
                reserva.cliente = cliente?.name ?: "Desconhecido"
                reserva.contato = cliente?.phone ?: "Sem contato"
                reserva.valor = valorReserva * reserva.vagas

                Log.d("DEBUG", "Reserva adicionada: $reserva")
                listaReserve.add(reserva)
            }

            listaResultado(listaReserve)
        }
    }
}
