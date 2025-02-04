package com.example.mvvmuni.ui.homepet.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.mvvmuni.core.network.RetrofitHelper
import com.example.mvvmuni.ui.homepet.data.model.Pet

class PetRepository {
    private val petService = RetrofitHelper.getPetService

    suspend fun fetchPets(): Result<List<Pet>> {
        return try {
            val response = petService.getPets()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Response body es null"))
            } else {
                Result.failure(Exception("Error en la petición: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addPet(pet: Pet): Result<Pet> {
        return try {
            val response = petService.addPet(pet)
            if (response.isSuccessful) {
                Log.i("PetRepository", "se creo")
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Response body es null"))
            } else {
                Result.failure(Exception("Error en la petición: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
