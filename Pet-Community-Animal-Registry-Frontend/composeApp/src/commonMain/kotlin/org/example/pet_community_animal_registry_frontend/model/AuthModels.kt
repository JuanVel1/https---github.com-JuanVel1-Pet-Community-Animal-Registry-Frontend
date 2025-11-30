package org.example.pet_community_animal_registry_frontend.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val user: User? = null)

@Serializable
data class RegisterRequest(val name: String, val email: String, val password: String)
