package com.example.playlistmaker

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.track_view, parent, false)
    ) {

    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L).format(model.trackTime)

        val roundedCornersValue=2f
        Glide
            .with(trackImage)
            .load(model.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(roundedCornersValue, trackImage)))
            .into(trackImage)
    }

    private fun dpToPx(dp: Float, context: ImageView): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}