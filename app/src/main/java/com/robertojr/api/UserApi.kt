package com.robertojr.api


import com.robertojr.moov.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("users")
    suspend fun findAll() : List<User>

    @GET("users/{id}")
    suspend fun findById(@Path("id") id: Long) : Call<User>

    @POST("users")
    suspend fun insert(@Body login:User): Call<User>

    @PUT("users/{id}")
    suspend fun updateById(@Body login:User , @Path("id") id:Long): Call<User>

    @DELETE("users/{id}")
    suspend fun deleteById(@Path("id") id:Long): Call<User>
}