package com.example.mvvmuni.ui.countersteps.data.repository

import android.util.Log
import com.example.mvvmuni.core.network.RetrofitHelper
import com.example.mvvmuni.ui.countersteps.data.model.CounterStepsRegister
import com.example.mvvmuni.ui.countersteps.data.model.CounterStepsDTO

class CounterStepsRepository() {

    private val counterStepsService = RetrofitHelper.registerCounterStepsService

    suspend fun getRegisterSteps():Result<List<CounterStepsDTO>>{
        Log.d("CounterRepository", "llego")
        return try {
            val response = counterStepsService.getResisterSteps()
            if (response.isSuccessful) {
                Log.d("CounterRepository", "Respuesta GET exitosa: ${response.body()}")
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Response body es null"))
            } else {
                Log.e("CounterRepository", "Error en GET respuesta: ${response.errorBody()?.string()}")
                Result.failure(Exception("Error en la petición: ${response.message()}"))

            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerCounterSteps(namePet:String, steps:Int):Result<CounterStepsDTO>{
        return try {
            val response = counterStepsService.counterStepsRegister(CounterStepsRegister(namePet,steps))
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Log.d("CounterRepository", "Respuesta exitosa: $body")
                Result.success(body)
            } else {
                Log.e("CounterRepository", "Error en respuesta: ${response.errorBody()?.string()}")
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }

        } catch (e: Exception) {
            Log.e("CounterRepository", "Excepción en la petición: ${e.message}")
            Result.failure(e)
        }
    }

}