package com.example.mvvmuni.ui.login.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.mvvmuni.ui.login.data.model.UserDTO
import com.example.mvvmuni.ui.login.data.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _loginError = MutableLiveData<String?>()
    val loginError: LiveData<String?> = _loginError

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val loginRepository = LoginRepository()

    fun onLoginChanged(username: String, password: String) {
        _username.value = username
        _password.value = password
        _loginEnable.value = username.isNotBlank() && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length >= 6

    fun onLoginSelected() {
        val currentUser = _username.value.orEmpty()
        val currentPassword = _password.value.orEmpty()

        viewModelScope.launch {
            val result = loginRepository.loginUser(currentUser, currentPassword)

            result.onSuccess {
                Log.d("LoginViewModel", "Inicio de sesión exitoso: $it")
                _loginSuccess.value = true
                _loginError.value = null
            }.onFailure {
                if(it.message == "timeout"){
                    _loginError.value = "Timeout del servidor"
                }
                Log.e("LoginViewModel", "Error en login: ${it.message}")
                _loginError.value = "Credenciales incorrectas"
            }
        }
    }

    fun clearLoginError() {
        _loginError.value = null
    }
}
