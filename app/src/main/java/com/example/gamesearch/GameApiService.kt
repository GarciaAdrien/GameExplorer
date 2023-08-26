package com.example.gamesearch

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApiService {
    // Endpoint for the platforms
    @GET("platforms?key=494bdf04e04a41ab80ef64893b1f60b2")
    suspend fun getPlatforms(): Response<PlatformListResponse>

    // Endpoint for the games (use the platform slug to get games specific to that platform)
    @GET("games?key=494bdf04e04a41ab80ef64893b1f60b2")
    suspend fun getGames(@Query("page") page: Int): Response<GameListResponse>

    @GET("games/{id}/movies?key=494bdf04e04a41ab80ef64893b1f60b2")
    suspend fun getGameVideos(@Path("id") gameId: String): Response<GameVideoResponse>

    //@GET("games/{id}?key=494bdf04e04a41ab80ef64893b1f60b2")
    //suspend fun getGameFocus(@Path("id") gameId: String): Response<GameFocus>

}


//get les videos par rapport al 'id
    //https://api.rawg.io/api/games/3498/movies?key=494bdf04e04a41ab80ef64893b1f60b2



