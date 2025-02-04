package com.example.mvvmuni.ui.login.data.datasource

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.mvvmuni.ui.login.data.model.LoginUser
import com.example.mvvmuni.ui.login.data.model.UserDTO
import okhttp3.Request


interface LoginService {
    /*@GET("users/{username}")
    suspend fun validateUsername(@Path("username") username : String) : Response<UsernameValidateDTO>
    */

    @POST("users/login")
    suspend fun loginUser(@Body request: LoginUser) : Response<UserDTO>

}