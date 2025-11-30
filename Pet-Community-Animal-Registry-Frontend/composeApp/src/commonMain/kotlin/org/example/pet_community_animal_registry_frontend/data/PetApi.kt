package org.example.pet_community_animal_registry_frontend.data

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.pet_community_animal_registry_frontend.model.LoginRequest
import org.example.pet_community_animal_registry_frontend.model.LoginResponse
import org.example.pet_community_animal_registry_frontend.model.Pet
import org.example.pet_community_animal_registry_frontend.model.RegisterRequest
import org.example.pet_community_animal_registry_frontend.model.User
import org.example.pet_community_animal_registry_frontend.network.KtorClient

class PetApi {
    private val client = KtorClient.client

    suspend fun register(request: RegisterRequest): User {
        return client.post("api/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        return client.post("api/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getPets(token: String): List<Pet> {
        return client.get("api/pets") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun addPet(token: String, pet: Pet): Pet {
        return client.post("api/pets") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(pet)
        }.body()
    }

    suspend fun updatePet(token: String, pet: Pet): Pet {
        return client.put("api/pets/${pet.id}") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(pet)
        }.body()
    }

    suspend fun deletePet(token: String, id: Int) {
        client.delete("api/pets/$id") {
            header("Authorization", "Bearer $token")
        }
    }
}
