package com.example.playlistmaker.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.models.PlaylistInfo

class PlaylistInStringViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.playlist_in_string_view, parentView, false)
) {

    private val name: TextView = itemView.findViewById(R.id.playlist_in_string_name_tv)
    private val songsNumber: TextView = itemView.findViewById(R.id.playlist_in_string_track_num_tv)
    private val image: ImageView = itemView.findViewById(R.id.playlist_in_string_image_iv)

    fun bind(model: PlaylistInfo) {
        name.text = model.playlistName
        songsNumber.text = model.tracksNum.toString()

        Glide
            .with(image)
            .load(model.artworkUrl512)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(image.resources.getDimensionPixelSize(R.dimen.playlist_image_corner_radius)))
            .into(image)
    }
}