package com.robertojr.moov.model

data class Reserve(
    val customerId: Long,
    val dataReserve: String,
    val id: Long?,
    val racerId: Long?,
    val vacancy: Int


) {
    override fun toString(): String {
        return "Reserve(customerId=$customerId, dataReserve='$dataReserve', id=$id, racerId=$racerId, vacancy=$vacancy)"
    }
}