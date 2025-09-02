package com.robertojr.moov.model.sending

data class ReserveSending(
    val customer: Customer?,
    val dataReserve: String?,
    val racer: Racer?,
    val vacancy: Int?
) {
    override fun toString(): String {
        return "ReserveSending(customer=${customer?.id}, dataReserve=$dataReserve, racer=${racer?.id}, vacancy=$vacancy)"
    }
}