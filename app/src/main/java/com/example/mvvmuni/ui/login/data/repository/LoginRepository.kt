package com.example.mvvmuni.ui.login.data.repository

import com.example.mvvmuni.core.network.RetrofitHelper
import com.example.mvvmuni.ui.login.data.model.LoginUser
import com.example.mvvmuni.ui.login.data.model.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import com.example.mvvmuni.ui.login.data.datasource.LoginService
import android.util.Log

class LoginRepository() {

    private val loginService = RetrofitHelper.loginService

    suspend fun loginUser(username: String, password: String): Result<UserDTO> {
        return try {
            Log.d("LoginRepository", "Enviando petición con user: $username y password: $password")

            val response = loginService.loginUser(LoginUser(username, password))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Log.d("LoginRepository", "Respuesta exitosa: $body")
                Result.success(body)
            } else {
                Log.e("LoginRepository", "Error en respuesta: ${response.errorBody()?.string()}")
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("LoginRepository", "Excepción en la petición: ${e.message}")
            Result.failure(e)
        }
    }


}