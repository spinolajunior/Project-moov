package com.robertojr.moov.model

import com.robertojr.api.CustomerApi
import com.robertojr.api.DriverApi
import com.robertojr.api.HistoricoApi
import com.robertojr.api.LoginApi
import com.robertojr.api.RacerApi
import com.robertojr.api.ReserveApi
import com.robertojr.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginRetrofit : LoginApi by lazy {
        retrofit.create(LoginApi::class.java)
    }

    val customerRetrofit : CustomerApi by lazy{
        retrofit.create(CustomerApi::class.java)
    }

    val driverRetrofit : DriverApi by lazy{
        retrofit.create(DriverApi::class.java)
    }

    val racerRetrofit : RacerApi by lazy {
        retrofit.create(RacerApi::class.java)
    }

    val reserveApi : ReserveApi by lazy {
        retrofit.create(ReserveApi::class.java)
    }

    val userRetrofit : UserApi by lazy{
        retrofit.create(UserApi::class.java)
    }

    val historicoRetrofit : HistoricoApi by lazy {
        retrofit.create(HistoricoApi::class.java)
    }



}