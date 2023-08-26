package com.example.gamesearch

data class GameVideoResponse(
    val results: List<GameVideo>
)

data class GameVideo(
    val id: Int,
    val name: String,
    val preview: String,
    val video_id: String,
)