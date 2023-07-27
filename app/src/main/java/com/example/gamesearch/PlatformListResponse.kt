package com.example.gamesearch

data class PlatformListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Game> // Utilisez la classe Game au lieu de Platform pour la liste de jeux
)
