package org.example.pet_community_animal_registry_frontend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.pet_community_animal_registry_frontend.data.PetApi
import org.example.pet_community_animal_registry_frontend.model.Pet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    token: String,
    pet: Pet?,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val api = remember { PetApi() }
    
    var name by remember { mutableStateOf(pet?.name ?: "") }
    var breed by remember { mutableStateOf(pet?.breed ?: "") }
    var status by remember { mutableStateOf(pet?.status ?: "Lost") }
    var contact by remember { mutableStateOf(pet?.contact ?: "") }
    var photoUrl by remember { mutableStateOf(pet?.photoUrl ?: "") }
    
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (pet == null) "Add Pet" else "Edit Pet") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Breed") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status (Lost/Found/Home)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Contact Info") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = photoUrl,
                onValueChange = { photoUrl = it },
                label = { Text("Photo URL") },
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            error = null
                            try {
                                val newPet = Pet(
                                    id = pet?.id,
                                    name = name,
                                    breed = breed,
                                    status = status,
                                    contact = contact,
                                    photoUrl = photoUrl.ifBlank { null },
                                    userId = pet?.userId ?: 0 // Backend should handle userId from token or we need to store it
                                )
                                if (pet == null) {
                                    api.addPet(token, newPet)
                                } else {
                                    api.updatePet(token, newPet)
                                }
                                onSave()
                            } catch (e: Exception) {
                                error = e.message ?: "Failed to save pet"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("Save")
                    }
                }
            }
        }
    }
}
