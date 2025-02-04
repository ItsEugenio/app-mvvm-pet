package com.example.mvvmuni.ui.register.presentacion.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mvvmuni.ui.register.presentacion.viewmodel.RegisterViewModel

class HomeRegisterScreen :ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen(RegisterViewModel())
        }
    }
}