package com.robertojr.api

import com.robertojr.moov.model.Historico
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HistoricoApi {

    @GET("historicos/driver/{id}")
    suspend fun findByDriver(@Path("id") id:Long): Response<List<Historico>>

    @GET("historicos/customer/{id}")
    suspend fun findByCustomer(@Path("id") id: Long): Response<List<Historico>>
}