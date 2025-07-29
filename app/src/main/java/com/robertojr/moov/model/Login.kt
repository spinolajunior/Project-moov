package com.robertojr.moov.model

import com.google.gson.annotations.SerializedName

data class Login(

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("username")
    val userName: String? =null
)