package com.example.playlistmaker.core.presentation.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.core.domain.models.Playlist

interface PlaylistItemClickListener {
    fun onClick(item: Playlist)
}

class PlaylistAdapter(
    private val onItemClickListener: PlaylistItemClickListener
) : RecyclerView.Adapter<PlaylistInGridViewHolder>() {

    var items: MutableList<Playlist> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistInGridViewHolder {
        return PlaylistInGridViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistInGridViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(items[position])
        }
    }

    override fun getItemCount() = items.size
}