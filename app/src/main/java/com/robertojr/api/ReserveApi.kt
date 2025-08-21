package com.robertojr.api


import com.robertojr.moov.model.Reserve
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReserveApi {

    @GET("reserves")
    suspend fun findAll() : List<Reserve>

    @GET("reserves/{id}")
    suspend fun findById(@Path("id") id: Long) : Call<Reserve>

    @POST("reserves")
    suspend fun insert(@Body login:Reserve): Call<Reserve>

    @PUT("reserves/{id}")
    suspend fun updateById(@Body login:Reserve , @Path("id") id:Long): Call<Reserve>

    @DELETE("reserves/{id}")
    suspend fun deleteById(@Path("id") id:Long): Call<Reserve>
}