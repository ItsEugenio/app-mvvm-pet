package com.example.mvvmuni.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.mvvmuni.ui.login.data.datasource.LoginService
import com.example.mvvmuni.ui.homepet.data.datasource.PetApiService
import com.example.mvvmuni.ui.register.data.datasource.RegisterApiService


object RetrofitHelper {
    private const val BASE_URL = "https://apipets.onrender.com/api/"

    private const val BASE_URL_PET = "https://apipets.onrender.com/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitpet: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PET)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginService: LoginService by lazy{
        retrofit.create(LoginService::class.java)
    }

    val getPetService: PetApiService by lazy {
        retrofitpet.create(PetApiService::class.java)
    }

    val registerService: RegisterApiService by lazy {
        retrofit.create(RegisterApiService::class.java)
    }
}