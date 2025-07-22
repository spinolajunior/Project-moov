package com.robertojr.moov.model

data class Driver(
    val ager: Int,
    val available: String,
    val carModel: String,
    val credentialId: Int,
    val description: String,
    val id: Int,
    val lastName: String,
    val name: String,
    val phone: String,
    val photoProfile: String,
    val plateNumber: String,
    val racersId: List<Int>,
    val ratingNumber: Double
)