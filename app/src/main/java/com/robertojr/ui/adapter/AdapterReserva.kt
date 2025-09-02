package com.robertojr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robertojr.moov.databinding.ReservasLayouthBinding

class AdapterReserva(private val context: Context, private val lista: MutableList<Reserva>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    fun atualizarLista(novaLista: MutableList<Reserva>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false) as TextView)
        } else {
            val binding = ReservasLayouthBinding.inflate(LayoutInflater.from(context), parent, false)
            ReserveViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.header.text = "Reservas"
        } else if (holder is ReserveViewHolder) {
            val reserva = lista[position]
            holder.cliente.text = reserva.cliente
            holder.contato.text = reserva.contato
            holder.vagas.text = reserva.vagas.toString()
            holder.valor.text = reserva.valor.toString()
        }
    }

    override fun getItemCount(): Int {
        return if (lista.isEmpty()) 0 else lista.size
    }

    inner class ReserveViewHolder(binding: ReservasLayouthBinding) : RecyclerView.ViewHolder(binding.root) {
        val cliente = binding.txtCliente
        val contato = binding.txtContato
        val vagas = binding.txtVagasReservadas
        val valor = binding.txtValorTotal
    }

    inner class HeaderViewHolder(val header: TextView) : RecyclerView.ViewHolder(header)
}
