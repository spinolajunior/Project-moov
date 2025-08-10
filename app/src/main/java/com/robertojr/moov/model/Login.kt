package com.robertojr.moov.model

import com.google.gson.annotations.SerializedName

data class Login(

    @SerializedName("email")
    var email: String? =null,

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("userName")
    val userName: String? = null
)