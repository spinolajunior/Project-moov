package com.robertojr.api


import com.robertojr.moov.model.Racer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RacerApi {

    @GET("racers")
    suspend fun findAll() : List<Racer>

    @GET("racers/{id}")
    suspend fun findById(@Path("id") id: Long) : Call<Racer>

    @POST("racers")
    suspend fun insert(@Body login:Racer): Call<Racer>

    @PUT("racers/{id}")
    suspend fun updateById(@Body login:Racer , @Path("id") id:Long): Call<Racer>

    @DELETE("racers/{id}")
    suspend fun deleteById(@Path("id") id:Long): Call<Racer>
}