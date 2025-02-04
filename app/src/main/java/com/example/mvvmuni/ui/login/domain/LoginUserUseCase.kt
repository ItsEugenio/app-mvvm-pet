package com.example.mvvmuni.ui.login.domain

import com.example.mvvmuni.ui.login.data.model.LoginUser
import com.example.mvvmuni.ui.login.data.model.UserDTO
import com.example.mvvmuni.ui.login.data.repository.LoginRepository

class LoginUserUseCase {
    private val repository = LoginRepository()

//    suspend operator fun invoke(user:LoginUser) : Result<UserDTO> {
//        val result = repository.loginUser(user)
//
//        return result
//    }
}