package com.example.gamesearch

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
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
    private var isFetchingData = false
    private lateinit var editTextSearch: EditText
    private var originalGameList: List<Game> = emptyList()
    private var preloadedGameList: List<Game> = emptyList()
    private lateinit var buttonSearch2: Button // Ajout de la variable
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerViewGames = findViewById<RecyclerView>(R.id.recyclerViewGames)
        progressBar = findViewById(R.id.progressBar)
        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch2 = findViewById(R.id.buttonSearch2) // Initialisation de la variable

        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        // Dans la fonction onCreate
        buttonSearch.setOnClickListener {
            val searchText = editTextSearch.text.toString().trim()
            if (searchText.isNotEmpty()) {
                val filteredGames = originalGameList.filter { it.name.contains(searchText, true) }
                if (filteredGames.isNotEmpty()) {
                    gameAdapter?.updateData(filteredGames)
                } else {
                    Toast.makeText(this, "Aucun jeu trouvé.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // If the search text is empty, reset the list to the original
                gameAdapter?.updateData(originalGameList)
            }

            if (gameList.isEmpty() && !isFetchingData) {
                isFetchingData = true
                progressBar.visibility = View.VISIBLE // Afficher la ProgressBar
                GlobalScope.launch(Dispatchers.Main) {
                    fetchAllGames()
                    isFetchingData = false
                    editTextSearch.visibility = View.VISIBLE // Affiche la barre de recherche
                    progressBar.visibility = View.GONE // Masquer la ProgressBar après le chargement
                }
            } else if (gameList.isNotEmpty()) {
                Toast.makeText(this, "La liste des jeux a bien été chargée", Toast.LENGTH_SHORT).show()
                editTextSearch.visibility = View.VISIBLE // Affiche la barre de recherche
            }
        }


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

        buttonSearch2.setOnClickListener {
            val searchText = editTextSearch.text.toString().trim()
            if (searchText.isNotEmpty()) {
                val filteredGames = preloadedGameList.filter { it.name.contains(searchText, true) }
                if (filteredGames.isNotEmpty()) {
                    gameAdapter?.updateData(filteredGames)
                } else {
                    Toast.makeText(this, "Aucun jeu trouvé.", Toast.LENGTH_SHORT).show()
                }
            } else {
                gameAdapter?.updateData(preloadedGameList)
            }
        }

        // Mise en place du TextWatcher pour la recherche en temps réel
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                val filteredGames = preloadedGameList.filter { it.name.contains(searchText, true) }
                gameAdapter?.updateData(filteredGames)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private suspend fun fetchAllGames() {
        val totalPageCount = 20

        for (page in 1..totalPageCount) {
            try {
                val gameResponse = gameApiService.getGames(page)
                if (gameResponse.isSuccessful) {
                    val gameListResponse = gameResponse.body()
                    if (gameListResponse != null) {
                        val gamesForPlatform = gameListResponse.results
                        gameList.addAll(gamesForPlatform)
                    }
                } else {
                    Log.e("API Error", gameResponse.message())
                }
            } catch (e: Exception) {
                Log.e("Exception", e.message ?: "Unknown error")
            }
        }

        gameList.sortBy { it.name }
        originalGameList = gameList.toList()
        preloadedGameList = gameList.toList()
        gameAdapter?.updateData(gameList)
    }
}
