package com.example.playlistmaker.core.presentation.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.core.domain.models.Playlist


class PlaylistInStringAdapter(
    private val onItemClickListener: PlaylistItemClickListener
) : RecyclerView.Adapter<PlaylistInStringViewHolder>() {

    var items: MutableList<Playlist> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistInStringViewHolder {
        return PlaylistInStringViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistInStringViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(items[position])
        }
    }

    override fun getItemCount() = items.size
}