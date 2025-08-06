package com.robertojr.moov.model

import com.google.gson.annotations.SerializedName

data class Login(

    @SerializedName("email")
    val email: String?,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("password")
    val password: String,

    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("userName")
    val userName: String
)