package com.example.mvvmuni.ui.countersteps.representation.screen

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmuni.ui.countersteps.data.repository.CounterStepsRepository
import com.example.mvvmuni.ui.countersteps.representation.viewmodel.StepCounterViewModel
import com.example.mvvmuni.ui.countersteps.representation.viewmodel.StepCounterViewModelFactory

class HomeCounterStepsScreen : ComponentActivity() {
    private lateinit var stepCounterViewModel: StepCounterViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                stepCounterViewModel.startListening()
            } else {
                // Mostrar mensaje si el usuario no concede el permiso
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear la instancia del repositorio (ajústalo a tu implementación)
        val repository = CounterStepsRepository()

        // Crear la instancia del ViewModelFactory con application
        val factory = StepCounterViewModelFactory(application, repository)
        stepCounterViewModel = ViewModelProvider(this, factory).get(StepCounterViewModel::class.java)

        val petName = intent.getStringExtra("PET_NAME") ?: "Sin Nombre"

        // Verificar permisos antes de iniciar el sensor de pasos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            } else {
                stepCounterViewModel.startListening()
            }
        } else {
            stepCounterViewModel.startListening()
        }

        setContent {
            CounterStepsScreen(viewModel = stepCounterViewModel, petName)
        }
    }
}
