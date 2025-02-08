package com.example.mvvmuni.ui.countersteps.representation.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.*
import com.example.mvvmuni.ui.countersteps.data.model.CounterStepsDTO
import com.example.mvvmuni.ui.countersteps.data.repository.CounterStepsRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel

class StepCounterViewModel(application: Application, private val repository: CounterStepsRepository) :
    AndroidViewModel(application), SensorEventListener {

    private val sensorManager = application.getSystemService(SensorManager::class.java)
    private var stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _stepCount = MutableLiveData(0)
    val stepCount: LiveData<Int> = _stepCount

    private val _isTracking = MutableLiveData(false)
    val isTracking: LiveData<Boolean> = _isTracking

    private var initialStepCount: Int? = null

    private val _stepsHistory = MutableLiveData<List<CounterStepsDTO>>()
    val stepsHistory: LiveData<List<CounterStepsDTO>> = _stepsHistory

    private val _isDialogOpen = MutableLiveData(false)
    val isDialogOpen: LiveData<Boolean> = _isDialogOpen

    private val _stepsSinceStart = MutableLiveData(0)
    val stepsSinceStart: LiveData<Int> = _stepsSinceStart

    init {
        fetchStepsHistory()
    }

    fun fetchStepsHistory() {
        viewModelScope.launch {
            val result = repository.getRegisterSteps()
            result.onSuccess {
                _stepsHistory.postValue(it)
                Log.d("StepCounterViewModel", "📜 Historial de pasos cargado con ${it.size} registros")
            }.onFailure {
                Log.e("StepCounterViewModel", "❌ Error al obtener historial: ${it.message}")
            }
        }
    }

    fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            initialStepCount = null
            _stepCount.value = 0
            _isTracking.value = true
            Log.d("StepCounterViewModel", "🔄 Contador de pasos iniciado...")
        } ?: Log.e("StepCounterViewModel", "❌ Sensor de pasos no disponible")
    }

    fun stopCounterAndRegister(namePet: String) {
        sensorManager.unregisterListener(this)
        _isTracking.value = false
        val pasosDesdeInicio = _stepCount.value ?: 0
        _stepCount.value = 0
        initialStepCount = null

        viewModelScope.launch {
            repository.registerCounterSteps(namePet, pasosDesdeInicio).onSuccess {
                Log.d("StepCounterViewModel", "✅ Pasos guardados correctamente")
                fetchStepsHistory()
            }.onFailure {
                Log.e("StepCounterViewModel", "❌ Error al guardar pasos: ${it.message}")
            }
        }
    }

    fun openDialog() {
        _isDialogOpen.value = true
    }

    fun closeDialog() {
        _isDialogOpen.value = false
    }

    fun resetCounter() {
        _stepCount.value = 0
        initialStepCount = null
        _isDialogOpen.value = false
        Log.d("StepCounterViewModel", "🔄 Contador de pasos reiniciado a 0")
    }


    fun stopListening() {
        sensorManager.unregisterListener(this)
        _isTracking.value = false
        _stepCount.value = 0
        initialStepCount = null
        Log.d("StepCounterViewModel", "🛑 Contador de pasos detenido")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val pasosAcumulados = it.values[0].toInt()
            if (initialStepCount == null) {
                initialStepCount = pasosAcumulados
            }
            val pasosDesdeInicio = pasosAcumulados - (initialStepCount ?: 0)
            _stepCount.value = pasosDesdeInicio
            Log.d("StepCounterViewModel", "📈 Pasos totales: $pasosAcumulados | Sesión: $pasosDesdeInicio")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.w("StepCounterViewModel", "⚠ Precisión del sensor cambiada: $accuracy")
    }
}
