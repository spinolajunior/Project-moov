package com.robertojr.api


import com.robertojr.moov.model.Reserve
import com.robertojr.moov.model.sending.ReserveSending
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReserveApi {

    @GET("reserves")
    suspend fun findAll() : Response<List<Reserve>>

    @GET("reserves/{id}")
    suspend fun findById(@Path("id") id: Long) : Response<Reserve>

    @POST("reserves")
    suspend fun insert(@Body reserve: ReserveSending): Response<Reserve>

    @PUT("reserves/{id}")
    suspend fun updateById(@Body reserve:Reserve , @Path("id") id:Long): Response<Reserve>

    @DELETE("reserves/{id}")
    suspend fun deleteById(@Path("id") id:Long): Response<Reserve>
}