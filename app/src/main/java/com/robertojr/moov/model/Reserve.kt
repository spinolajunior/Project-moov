package com.robertojr.moov.model

data class Reserve(
    val customerId: Int,
    val dataReserve: String,
    val id: Int,
    val racerId: Int,
    val vacancy: Int


) {
    override fun toString(): String {
        return "Reserve(customerId=$customerId, dataReserve='$dataReserve', id=$id, racerId=$racerId, vacancy=$vacancy)"
    }
}