package com.robertojr.api


import com.robertojr.moov.model.Driver

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DriverApi {

    @GET("drivers")
    suspend fun findAll(): List<Driver>

    @GET("drivers/{id}")
    suspend fun findById(@Path("id") id: Long): Call<Driver>

    @POST("drivers")
    suspend fun insert(@Body driver: Driver): Response<Driver>

    @PUT("drivers/{id}")
    suspend fun updateById(@Body driver: Driver, @Path("id") id: Long?): Response<Driver>

    @DELETE("drivers/{id}")
    suspend fun deleteById(@Path("id") id: Long): Call<Driver>
}