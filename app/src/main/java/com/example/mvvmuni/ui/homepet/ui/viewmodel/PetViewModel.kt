package com.example.mvvmuni.ui.homepet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmuni.ui.homepet.data.repository.PetRepository
import com.example.mvvmuni.ui.homepet.data.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PetViewModel : ViewModel() {
    private val repository = PetRepository()

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchPets() {
        viewModelScope.launch {
            val result = repository.fetchPets()

            result.onSuccess { petList ->
                _pets.value = petList
                _error.value = null
            }.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    fun addPet(name: String, type: String, range: String) {
        val newPet = Pet(id = "", namePet = name, typePet = type, rangePet = range)
        viewModelScope.launch {
            val result = repository.addPet(newPet)
            result.onSuccess { pet ->
                _pets.value = _pets.value + pet
            }.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }
}
