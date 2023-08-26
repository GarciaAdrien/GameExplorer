package com.example.gamesearch

import com.example.gamesearch.GameDetailActivity
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import java.text.SimpleDateFormat
import java.util.Locale

class GameAdapter(private var gameList: List<Game>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_game, parent, false)
        return GameViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val currentGame = gameList[position]

        holder.textViewGameName.text = currentGame.name
        val releasedDate = formatDate(currentGame.released)
        holder.textViewReleased.text = ("Date de sortie $releasedDate")
        val platforms = currentGame.platforms.joinToString(", ") { it.platform.name }
        holder.textViewPlatforms.text = platforms
        // Clear Glide cache before loading the image
        Glide.with(holder.itemView)
            .clear(holder.imageViewBackground)

// Create a transition factory to enable cross-fading between placeholder and loaded images
        val crossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

        Glide.with(holder.itemView)
            .load(currentGame.background_image)
            .transform(ImagePlaceholderTransformation()) // Use the custom transformation
            .transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
            .into(holder.imageViewBackground)

        holder.buttonViewAction.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, GameDetailActivity::class.java)
            intent.putExtra("gameId", currentGame.id.toString()) // Make sure currentGame.id is a String
            context.startActivity(intent)
        }

    }
    // Function to format the date in "dd/MM/yyyy" format
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }



    override fun getItemCount() = gameList.size

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonViewAction: Button = itemView.findViewById(R.id.buttonAction)
        val textViewPlatforms: TextView = itemView.findViewById(R.id.textViewPlatforms)
        val imageViewBackground: ImageView = itemView.findViewById(R.id.imageViewBackground)
        val textViewGameName: TextView = itemView.findViewById(R.id.textViewGameName)
        val textViewReleased: TextView = itemView.findViewById(R.id.textViewReleased)
    }
    fun updateData(newGameList: List<Game>) {
        gameList = newGameList
        notifyDataSetChanged()
    }
}
