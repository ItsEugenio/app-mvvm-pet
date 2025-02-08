package com.example.mvvmuni.ui.countersteps.representation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mvvmuni.ui.countersteps.data.model.CounterStepsDTO
import com.example.mvvmuni.ui.countersteps.representation.viewmodel.StepCounterViewModel

@Composable
fun CounterStepsScreen(viewModel: StepCounterViewModel, petName: String) {
    val stepCount by viewModel.stepCount.observeAsState(0)
    val isTracking by viewModel.isTracking.observeAsState(false)
    val showDialog by viewModel.isDialogOpen.observeAsState(false)
    val stepsHistory by viewModel.stepsHistory.observeAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pasos Contados: $stepCount de: $petName", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (isTracking) {
                viewModel.openDialog()
            } else {
                viewModel.startListening()
            }
        }) {
            Text(if (isTracking) "Detener" else "Iniciar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección del historial de pasos
        Text("Historial de Pasos", style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(stepsHistory) { step ->
                StepCard(step)
            }
        }
    }

    if (showDialog) {
        StopCounterDialog(
            onConfirm = { viewModel.stopCounterAndRegister(petName) },
            onDismiss = { viewModel.closeDialog() },
            onClose = { viewModel.resetCounter() }
        )
    }
}

@Composable
fun StopCounterDialog(onConfirm: () -> Unit, onDismiss: () -> Unit, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* No se cierra automáticamente */ },
        title = { Text("Confirmación") },
        text = { Text("¿Quieres detener el contador de pasos y registrar los datos?") },
        confirmButton = {
            Column {
                Button(onClick = onConfirm) {
                    Text("Guardar y detener")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onClose) {
                    Text("Cerrar y reiniciar")
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}



@Composable
fun StepCard(step: CounterStepsDTO) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🐶 Mascota: ${step.namePet}", style = MaterialTheme.typography.bodyLarge)
            Text("👣 Pasos: ${step.steps}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
