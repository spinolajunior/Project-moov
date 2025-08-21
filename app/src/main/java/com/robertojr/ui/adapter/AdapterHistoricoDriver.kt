package com.robertojr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robertojr.moov.databinding.HistoricoMotoristaLayouthBinding
import com.robertojr.moov.model.Historico

class AdapterHistoricoDriver(private val context: Context, private val listaHistorico : List<Historico>):

    RecyclerView.Adapter<AdapterHistoricoDriver.HistoricoViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoricoViewHolder {
        val historico = HistoricoMotoristaLayouthBinding.inflate(LayoutInflater.from(context),parent,false)
        return HistoricoViewHolder(historico)
    }

    override fun onBindViewHolder(
        holder: HistoricoViewHolder,
        position: Int
    ) {
        val historico = listaHistorico[position]

            holder.origem.text = historico.origem
            holder.destino.text = historico.destino
            holder.status.text = historico.status
            holder.motorista.text = historico.motorista
            holder.vagasTotais.text = historico.vagasTotais.toString()
            holder.vagasDisponiveis.text = historico.vagasDisponiveis.toString()
            holder.valorUnitario.text = historico.valorUnitario.toString()
            holder.data.text = historico.data

    }

    override fun getItemCount() = listaHistorico.size

    inner class HistoricoViewHolder(binding : HistoricoMotoristaLayouthBinding) : RecyclerView.ViewHolder(binding.root){

        val origem = binding.txtOrigem
        val destino = binding.txtDestino
        val status = binding.txtStatus
        val motorista = binding.txtMotorista

        val vagasTotais = binding.txtVagasTotais

        val vagasDisponiveis = binding.txtVagasDisponiveis
        val valorUnitario = binding.txtValorUnitario

        val data = binding.txtData

    }
}