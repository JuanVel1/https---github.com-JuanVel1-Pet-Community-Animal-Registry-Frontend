package org.example.pet_community_animal_registry_frontend

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.pet_community_animal_registry_frontend.model.Pet
import org.example.pet_community_animal_registry_frontend.ui.LoginScreen
import org.example.pet_community_animal_registry_frontend.ui.PetDetailScreen
import org.example.pet_community_animal_registry_frontend.ui.PetListScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var token by remember { mutableStateOf<String?>(null) }
        var currentScreen by remember { mutableStateOf("login") }
        var selectedPet by remember { mutableStateOf<Pet?>(null) }

        if (token == null) {
            LoginScreen(onLoginSuccess = { newToken ->
                token = newToken
                currentScreen = "list"
            })
        } else {
            when (currentScreen) {
                "list" -> PetListScreen(
                    token = token!!,
                    onLogout = { token = null },
                    onAddPet = {
                        selectedPet = null
                        currentScreen = "detail"
                    },
                    onPetClick = { pet ->
                        selectedPet = pet
                        currentScreen = "detail"
                    }
                )
                "detail" -> PetDetailScreen(
                    token = token!!,
                    pet = selectedPet,
                    onSave = { currentScreen = "list" },
                    onCancel = { currentScreen = "list" }
                )
                else -> {
                    currentScreen = "list"
                }
            }
        }
    }
}