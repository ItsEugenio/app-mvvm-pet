package com.example.mvvmuni.ui.homepet.data.datasource

import com.example.mvvmuni.ui.homepet.data.model.Pet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PetApiService {
    @GET("pets")
    suspend fun getPets(): Response<List<Pet>>

    @POST("pets")
    suspend fun addPet(@Body pet: Pet): Response<Pet>
}
