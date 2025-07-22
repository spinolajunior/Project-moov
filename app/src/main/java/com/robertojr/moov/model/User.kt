package com.robertojr.moov.model

data class User(
    val ager: Int,
    val credentialID: Int,
    val description: String,
    val id: Int,
    val lastName: String,
    val name: String,
    val phone: String,
    val photoProfile: String
)