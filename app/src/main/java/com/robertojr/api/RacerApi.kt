package com.robertojr.api


import com.robertojr.moov.model.Racer
import com.robertojr.moov.model.sending.RacerSending
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RacerApi {

    @GET("racers")
    suspend fun findAll() : Response<List<Racer>>

    @GET("racers/{id}")
    suspend fun findById(@Path("id") id: Long) : Response<Racer>

    @POST("racers")
    suspend fun insert(@Body corrida: RacerSending): Response<Racer>

    @PUT("racers/{id}")
    suspend fun updateById(@Body corrida:Racer , @Path("id") id:Long): Response<Racer>

    @DELETE("racers/{id}")
    suspend fun deleteById(@Path("id") id:Long): Response<Racer>
}