package com.example.mvvmuni.ui.homepet.ui.screen

import android.app.ProgressDialog.show
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvvmuni.ui.homepet.ui.viewmodel.PetViewModel
import com.example.mvvmuni.ui.homepet.data.model.Pet
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(viewModel: PetViewModel) {
    val pets by viewModel.pets.collectAsState()
    val error by viewModel.error.observeAsState()
    var show by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.fetchPets() }

    Scaffold(topBar = { TopAppBar(title = { Text("Lista de Mascotas") }) }, floatingActionButton = {
        FloatingActionButton(onClick = { show = true }) {
            Text(text = "Agregar")
        }
    }, floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            MyDialog(show,
                onDismiss = { show = false },
                onConfirm = { name, type, range ->
                    viewModel.addPet(name, type, range)
                })


            when {
                error != null -> Text(
                    text = "Error: ${error.orEmpty()}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )

                pets.isEmpty() -> CircularProgressIndicator()

                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(pets) { pet ->
                        PetCard(pet)
                    }
                }
            }
        }
    }
}


@Composable
fun PetCard(pet: Pet) {
    val petColor = getPetColor(pet.typePet)


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Línea de color en la parte superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .background(petColor)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Pets,
                        contentDescription = "Icono de ${pet.typePet}",
                        tint = petColor,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Nombre: ${pet.namePet}", style = MaterialTheme.typography.bodyLarge, fontSize = 30.sp
                    )
                }
                Text(text = "Tipo: ${pet.typePet}", fontSize = 20.sp)
                Text(text = "Rango: ${pet.rangePet}", fontSize = 20.sp)
            }
        }
    }
}


fun getPetColor(typePet: String): Color {
    return when (typePet.lowercase()) {
        "perro" -> Color.Blue
        "gato" -> Color(0xFFFF9800)
        "pez" -> Color.Cyan
        "tortuga" -> Color.Green
        else -> Color.Gray
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDialog(
    show: Boolean, onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit
) {
    if (show) {
        var name by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("Perro") }
        var range by remember { mutableStateOf("Cachorro") }
        val types = listOf("Perro", "Gato", "Pez", "Tortuga")
        val ranges = listOf("Cachorro", "Joven", "Adulto")
        var expandedType by remember { mutableStateOf(false) }
        var expandedRange by remember { mutableStateOf(false) }
        val isButtonEnabled = name.isNotBlank() && type.isNotEmpty() && range.isNotEmpty()

        AlertDialog(onDismissRequest = { onDismiss() }, confirmButton = {
            TextButton(onClick = { onConfirm(name, type, range)}, enabled = isButtonEnabled) {
                Text(text = "Agregar")
            }
        }, title = { Text(text = "Agregar Mascota") }, text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la mascota") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(expanded = expandedType,
                    onExpandedChange = { expandedType = it }) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de mascota") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedType,
                        onDismissRequest = { expandedType = false }) {
                        types.forEach { t ->
                            DropdownMenuItem(text = { Text(t) }, onClick = {
                                type = t
                                expandedType = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(expanded = expandedRange,
                    onExpandedChange = { expandedRange = it }) {
                    OutlinedTextField(
                        value = range,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Etapa de vida") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRange) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedRange,
                        onDismissRequest = { expandedRange = false }) {
                        ranges.forEach { r ->
                            DropdownMenuItem(text = { Text(r) }, onClick = {
                                range = r
                                expandedRange = false
                            })
                        }
                    }
                }
            }
        }, dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancelar")
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Preview
@Composable
fun PreDialog() {
    var nombreMascota by remember { mutableStateOf("") }
    var tipoMascota by remember { mutableStateOf("Perro") }
    var etapaVida by remember { mutableStateOf("Cachorro") }
    val tiposMascota = listOf("Perro", "Gato", "Pez", "Tortuga")
    val etapasVida = listOf("Cachorro", "Joven", "Adulto")
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedEtapa by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = {}, confirmButton = {
        TextButton(onClick = { /* Acción para agregar */ }) {
            Text(text = "Agregar")
        }
    }, title = { Text(text = "Agregar Mascota") }, text = {
        Column {
            OutlinedTextField(
                value = nombreMascota,
                onValueChange = { nombreMascota = it },
                label = { Text("Nombre de la mascota") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(expanded = expandedTipo,
                onExpandedChange = { expandedTipo = it }) {
                OutlinedTextField(
                    value = tipoMascota,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de mascota") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }) {
                    tiposMascota.forEach { tipo ->
                        DropdownMenuItem(text = { Text(tipo) }, onClick = {
                            tipoMascota = tipo
                            expandedTipo = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(expanded = expandedEtapa,
                onExpandedChange = { expandedEtapa = it }) {
                OutlinedTextField(
                    value = etapaVida,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Etapa de vida") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEtapa) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedEtapa,
                    onDismissRequest = { expandedEtapa = false }) {
                    etapasVida.forEach { etapa ->
                        DropdownMenuItem(text = { Text(etapa) }, onClick = {
                            etapaVida = etapa
                            expandedEtapa = false
                        })
                    }
                }
            }
        }
    }, dismissButton = {
        TextButton(onClick = { /* Acción para cancelar */ }) {
            Text(text = "Cancelar")
        }
    })
}


//@Preview(showBackground = true)
@Composable
fun PetCardPreview() {
    val samplePets = listOf(
        Pet(id = "1", namePet = "Bobby", typePet = "Perro", rangePet = "Mediano"),
        Pet(id = "2", namePet = "Milo", typePet = "Gato", rangePet = "Pequeño"),
        Pet(id = "3", namePet = "Rocky", typePet = "Loro", rangePet = "Pequeño")
    )

    LazyColumn {
        items(samplePets) { pet ->
            PetCard(pet = pet)
        }
    }
}
