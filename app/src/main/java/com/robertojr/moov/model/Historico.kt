package com.robertojr.moov.model

data class Historico(
    val corrida: Racer?,
    val data: String?,
    val destino: String?,
    val id: Long?,
    val motorista: String?,
    val origem: String?,
    val reserva: Reserve?,
    val status: String?,
    val vagasDisponiveis: Int?,
    val vagasReservadas: Int?,
    val vagasTotais: Int?,
    val valorTotal: Double?,
    val valorUnitario: Double?
) {
    override fun toString(): String {
        return "Historico(corrida=$corrida, data=$data, destino=$destino, id=$id, motorista=$motorista, origem=$origem, reserva=$reserva, status=$status, vagasDisponiveis=$vagasDisponiveis, vagasReservadas=$vagasReservadas, vagasTotais=$vagasTotais, valorTotal=$valorTotal, valorUnitario=$valorUnitario)"
    }
}