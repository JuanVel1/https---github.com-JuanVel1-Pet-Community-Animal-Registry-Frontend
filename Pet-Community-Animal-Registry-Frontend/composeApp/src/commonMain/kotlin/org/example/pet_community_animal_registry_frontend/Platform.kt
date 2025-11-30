package org.example.pet_community_animal_registry_frontend

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform