package org.example.pet_community_animal_registry_frontend.model

import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    val id: Int? = null,
    val name: String,
    val breed: String,
    val photoUrl: String? = null,
    val status: String, // e.g., "Lost", "Found", "Home"
    val contact: String,
    val userId: Int
)
