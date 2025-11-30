package org.example.pet_community_animal_registry_frontend.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String? = null // Only used for registration/login requests
)
