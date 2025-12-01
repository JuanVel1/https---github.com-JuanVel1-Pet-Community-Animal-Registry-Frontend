package org.example.pet_community_animal_registry_frontend.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.example.pet_community_animal_registry_frontend.data.PetApi
import org.example.pet_community_animal_registry_frontend.model.Pet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    token: String,
    onLogout: () -> Unit,
    onAddPet: () -> Unit,
    onPetClick: (Pet) -> Unit
) {
    val scope = rememberCoroutineScope()
    val api = remember { PetApi() }
    var pets by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var petToDelete by remember { mutableStateOf<Pet?>(null) }

    fun loadPets() {
        scope.launch {
            isLoading = true
            error = null
            try {
                pets = api.getPets(token)
            } catch (e: Exception) {
                error = e.message ?: "Falla al cargar las mascotas"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadPets()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de mascotas") },
                actions = {
                    androidx.compose.material3.TextButton(onClick = { loadPets() }) {
                        Text("↻")
                    }
                    androidx.compose.material3.TextButton(onClick = onLogout) {
                        Text("Salir")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPet) {
                Text("+")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = error!!, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.material3.Button(onClick = { loadPets() }) {
                        Text("Intentar de nuevo")
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pets) { pet ->
                        PetItem(
                            pet = pet,
                            onClick = { onPetClick(pet) },
                            onDelete = { petToDelete = pet }
                        )
                    }
                }
            }
        }
        
        // Delete confirmation dialog
        petToDelete?.let { pet ->
            AlertDialog(
                onDismissRequest = { petToDelete = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar a ${pet.name}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                try {
                                    api.deletePet(token, pet.id!!)
                                    petToDelete = null
                                    loadPets()
                                } catch (e: Exception) {
                                    error = e.message ?: "Error al eliminar mascota"
                                    petToDelete = null
                                }
                            }
                        }
                    ) {
                        Text("Confirmar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { petToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun PetItem(pet: Pet, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = pet.photoUrl ?: "https://placehold.co/100x100.png?text=Pet",
                contentDescription = pet.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = pet.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "${pet.breed} • ${pet.status}", style = MaterialTheme.typography.bodyMedium)
            }
            androidx.compose.material3.TextButton(onClick = onDelete) {
                Text("🗑️", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
