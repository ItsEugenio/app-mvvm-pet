package com.example.mvvmuni.ui.register.data.datasource

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.mvvmuni.ui.register.data.model.RegisterUser
import com.example.mvvmuni.ui.register.data.model.UserDTO

interface RegisterApiService {
    @POST("users/create")
    suspend fun registerUser(@Body registerUser: RegisterUser) : Response<UserDTO>
}