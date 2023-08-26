package com.example.gamesearch

data class GameListResponse(
 val count: Int,
 val next: String?,
 val previous: String?,
 val results: List<Game>
)

data class Game(
 val id: Int,
 val slug: String,
 val name: String,
 val released: String,
 val background_image: String,
 val rating: Double,
 val year_start: Int,
 val year_End: Int,
 val rating_top: Double,
 val ratings: List<Rating>,
 val platforms: List<Platform>,
 val games_count: Int,
)

data class Rating(
 val id: Int,
 val title: String,
 val count: Int,
 val percent: Double
)

data class Platform(
 val platform: PlatformInfo,
 val released_at: String?,
 val requirements_en: PlatformRequirements?,
 val requirements_ru: PlatformRequirements?
)

data class PlatformInfo(
 val id: Int,
 val name: String,
 val slug: String,
 val image: String?
)

data class PlatformRequirements(
 val minimum: String,
 val recommended: String
)
