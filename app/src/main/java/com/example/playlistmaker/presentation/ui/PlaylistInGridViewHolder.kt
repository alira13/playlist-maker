package com.example.playlistmaker.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

class PlaylistInGridViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.playlist_view, parentView, false)
) {

    private val name: TextView = itemView.findViewById(R.id.playlist_name_tv)
    private val songsNumber: TextView = itemView.findViewById(R.id.playlist_songs_number)
    private val image: ImageView = itemView.findViewById(R.id.playlist_image_iv)

    fun bind(model: Playlist) {
        name.text = model.playlistName
        songsNumber.text = getTrackAmount(model)

        Glide
            .with(image)
            .load(model.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(image.resources.getDimensionPixelSize(R.dimen.player_track_image_corner_radius))
            )
            .into(image)
    }

    private fun getTrackAmount(playlist: Playlist): String {
        return itemView.resources.getQuantityString(
            R.plurals.track_amount,
            playlist.tracksNum,
            playlist.tracksNum
        )
    }
}