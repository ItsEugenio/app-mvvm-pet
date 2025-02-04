package com.example.mvvmuni.ui.register.presentacion.viewmodel
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.mvvmuni.ui.register.data.repository.RegisterRepository

class RegisterViewModel:ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    private val _loginError = MutableLiveData<String?>()
    val loginError: LiveData<String?> = _loginError

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val registerRepository = RegisterRepository()

    fun onRegisterChanged(username: String, password: String, email:String) {
        _username.value = username
        _password.value = password
        _email.value = email
        _registerEnable.value = username.isNotBlank() && isValidPassword(password) && email.isNotBlank()
    }

    private fun isValidPassword(password: String): Boolean = password.length >= 6

    fun registerSelected(){
        val currentUser = _username.value.orEmpty()
        val currentPassword = _password.value.orEmpty()
        val currentEmail = _email.value.orEmpty()

        viewModelScope.launch {
            val result = registerRepository.registerUser(currentUser,currentPassword,currentEmail)

            result.onSuccess {
                Log.d("RegisterViewModel", "Registro con exitoso: $it")
                _loginSuccess.value = true
                _loginError.value = null
            }.onFailure {
                Log.e("RegisterViewModel", "Error en register: ${it.message}")
                _loginError.value = "algo fallo"
            }
        }

    }

    fun clearLoginError() {
        _loginError.value = null
    }


}