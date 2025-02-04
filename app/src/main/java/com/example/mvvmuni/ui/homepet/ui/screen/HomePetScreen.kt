package com.example.mvvmuni.ui.homepet.ui.screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mvvmuni.ui.homepet.ui.viewmodel.PetViewModel


class HomePetScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetListScreen(PetViewModel())
        }
    }
}