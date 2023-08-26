package com.example.gamesearch
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameDetailActivity : AppCompatActivity() {//, OnMapReadyCallback

    private lateinit var gameApiService: GameApiService
    private lateinit var googleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        gameApiService = retrofit.create(GameApiService::class.java)

        val gameId = intent.getStringExtra("gameId") ?: return

        GlobalScope.launch(Dispatchers.Main) {
            fetchGameDetails(gameId)
        }
        val buttonBackToList = findViewById<Button>(R.id.buttonBackToList)
        buttonBackToList.setOnClickListener {
            onBackPressed() // Go back to the previous activity (game list)
        }
        //val mapFragment = supportFragmentManager.findFragmentById(R.id.mapContainer) as? SupportMapFragment
        //mapFragment?.getMapAsync(this)
    }

/*
    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            // Set up map settings and markers as needed

            val gameLocation = LatLng(37.7749, -122.4194) // Example coordinates
            googleMap.addMarker(MarkerOptions().position(gameLocation).title("Game Location"))
            googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(gameLocation))
        }
    }
*/

    private suspend fun fetchGameDetails(gameId: String) {
        try {
            val gameVideoResponse = gameApiService.getGameVideos(gameId)
            Log.d("API Response", gameVideoResponse.toString())
            if (gameVideoResponse.isSuccessful) {
                val gameVideoData = gameVideoResponse.body()
                if (gameVideoData != null) {
                    val GameDetails = gameVideoData.results
                    updateUIWithGameDetails(GameDetails) // Update to use results
                }
            } else {
                Log.e("API Error", gameVideoResponse.message())
            }
        } catch (e: Exception) {
            Log.e("Exception", e.message ?: "Unknown error")
        }
    }




    private fun updateUIWithGameDetails(gameVideoData: List<GameVideo>) {
        val textViewGameTitle = findViewById<TextView>(R.id.textViewGameTitle)
        val imageViewPreview = findViewById<ImageView>(R.id.imageViewPreview)
        val textViewNoVideos = findViewById<TextView>(R.id.textViewNoVideos)

        if (gameVideoData.isNotEmpty()) {
            val firstGameVideo = gameVideoData[0]  // Extract the first video from the list
            Log.d("GameDetailActivity", "First video name: ${firstGameVideo.name}")
            Log.d("GameDetailActivity", "First video preview: ${firstGameVideo.preview}")
            // Display other game details as needed
            textViewGameTitle.text = firstGameVideo.name

            Glide.with(this)
                .load(firstGameVideo.preview)
                .into(imageViewPreview)

            // Now you can also iterate through the list of videos if needed
            for (gameVideo in gameVideoData) {
                val videoId = gameVideo.id
                val videoName = gameVideo.name
                val videoPreviewUrl = gameVideo.preview
                val videoVideoId = gameVideo.video_id

                // Update your UI to display these video details as needed
            }
        } else {
            textViewNoVideos.visibility = View.VISIBLE
            textViewNoVideos.text = "Aucune vidéo n'a été trouvée pour ce jeu."
            // Handle the case when there are no videos available
        }
    }


}
