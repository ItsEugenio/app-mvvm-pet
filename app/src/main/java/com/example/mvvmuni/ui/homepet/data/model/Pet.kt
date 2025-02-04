package com.example.mvvmuni.ui.homepet.data.model
import com.google.gson.annotations.SerializedName

data class Pet(
    @SerializedName("_id") val id: String,
    @SerializedName("namePet") val namePet: String,
    @SerializedName("typePet") val typePet: String,
    @SerializedName("rangePet") val rangePet: String
)
