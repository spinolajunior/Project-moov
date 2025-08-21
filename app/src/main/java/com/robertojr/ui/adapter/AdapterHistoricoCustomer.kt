package com.robertojr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robertojr.moov.databinding.HistoricoClienteLayouthBinding
import com.robertojr.moov.model.Historico

class AdapterHistoricoCustomer(private val context: Context , private val listaHistorico : List<Historico>):

    RecyclerView.Adapter<AdapterHistoricoCustomer.HistoricoViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoricoViewHolder {
        val historico = HistoricoClienteLayouthBinding.inflate(LayoutInflater.from(context),parent,false)
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
            holder.vagasReservadas.text = historico.vagasReservadas.toString()
            holder.valorUnitario.text = historico.valorUnitario.toString()
            holder.valorTotal.text = historico.valorTotal.toString()
            holder.data.text = historico.data

    }

    override fun getItemCount() = listaHistorico.size

    inner class HistoricoViewHolder(binding : HistoricoClienteLayouthBinding) : RecyclerView.ViewHolder(binding.root){

        val origem = binding.txtOrigem
        val destino = binding.txtDestino
        val status = binding.txtStatus
        val motorista = binding.txtMotorista
        val vagasReservadas = binding.txtVagasReservadas
        val valorUnitario = binding.txtValorUnitario
        val valorTotal = binding.txtValorTotal
        val data = binding.txtData

    }
}