package com.robertojr.api


import com.robertojr.moov.model.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LoginApi {

    @GET("logins")
    suspend fun findAll() : List<Login>

    @GET("logins/{id}")
    suspend fun findById(@Path("id") id: Long) : Call<Login>

    @POST("logins")
    suspend fun insert(@Body login:Login): Call<Login>

    @PUT("logins/{id}")
    suspend fun updateById(@Body login:Login , @Path("id") id:Long): Call<Login>

    @DELETE("logins/{id}")
    suspend fun deleteById(@Path("id") id:Long): Call<Login>
}