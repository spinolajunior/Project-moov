package com.robertojr.api


import com.robertojr.moov.model.Customer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerApi {

    @GET("customers")
    suspend fun findAll() : List<Customer>

    @GET("customers/{id}")
    suspend fun findById(@Path("id") id: Long) : Call<Customer>

    @POST("customers")
    suspend fun insert(@Body customer:Customer): Call<Customer>

    @PUT("customers/{id}")
    suspend fun updateById(@Body customer:Customer , @Path("id") id:Long): Call<Customer>

    @DELETE("customers/{id}")
    suspend fun deleteById(@Path("id") id:Long): Call<Customer>
}