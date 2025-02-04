package com.example.mvvmuni.ui.register.data.repository

import android.util.Log
import com.example.mvvmuni.core.network.RetrofitHelper
import com.example.mvvmuni.ui.register.data.model.RegisterUser
import com.example.mvvmuni.ui.register.data.model.UserDTO

class RegisterRepository() {
    private val registerService = RetrofitHelper.registerService

    suspend fun registerUser(user:String, password:String, email:String): Result<UserDTO>{
        return try {

            val response = registerService.registerUser(RegisterUser(user, password, email))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Log.d("RegisterRepository", "Respuesta exitosa: $body")
                Result.success(body)
            } else {
                Log.e("RegisterRepository", "Error en respuesta: ${response.errorBody()?.string()}")
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }

        } catch (e: Exception) {
            Log.e("RegisterRepository", "Excepción en la petición: ${e.message}")
            Result.failure(e)
        }
    }
}