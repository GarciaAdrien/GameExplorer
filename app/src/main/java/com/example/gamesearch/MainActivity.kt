package com.example.gamesearch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val gameList: MutableList<Game> = mutableListOf()

    private lateinit var gameApiService: GameApiService
    private var gameAdapter: GameAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerViewGames = findViewById<RecyclerView>(R.id.recyclerViewGames)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the API service
        gameApiService = retrofit.create(GameApiService::class.java)

        // Initialize the adapter with an empty list at the beginning
        gameAdapter = GameAdapter(emptyList())
        recyclerViewGames.layoutManager = LinearLayoutManager(this)
        recyclerViewGames.adapter = gameAdapter

        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        buttonSearch.setOnClickListener {
            // Call the API when the button is clicked
            GlobalScope.launch(Dispatchers.Main) {
                fetchAllGames()
            }
        }
    }

    private suspend fun fetchAllGames() {
        val totalPageCount = 5 // Set this to an appropriate value based on the total number of pages available

        for (page in 1..totalPageCount) {
            try {
                val gameResponse = gameApiService.getGames(page)
                if (gameResponse.isSuccessful) {
                    val gameListResponse = gameResponse.body()
                    if (gameListResponse != null) {
                        val gamesForPlatform = gameListResponse.results
                        // Add games to the main gameList
                        gameList.addAll(gamesForPlatform)
                    }
                } else {
                    // Log error message for debugging
                    Log.e("API Error", gameResponse.message())
                }
            } catch (e: Exception) {
                // Log exception for debugging
                Log.e("Exception", e.message ?: "Unknown error")
            }
        }

        // Sort the gameList by game names (ascending order)
        gameList.sortBy { it.name }

        // Update the adapter data with the new games
        gameAdapter?.updateData(gameList)
    }

}

