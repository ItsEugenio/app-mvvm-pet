package com.example.mvvmuni.ui.countersteps.data.datasource

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import com.example.mvvmuni.ui.countersteps.data.model.CounterStepsRegister
import com.example.mvvmuni.ui.countersteps.data.model.CounterStepsDTO

interface CounterStepsApiService {
    @POST("steps")
    suspend fun counterStepsRegister(@Body counterStepsRegister: CounterStepsRegister) : Response<CounterStepsDTO>

    @GET("steps")
    suspend fun getResisterSteps():Response<List<CounterStepsDTO>>

}