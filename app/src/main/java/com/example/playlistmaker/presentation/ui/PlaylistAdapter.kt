package com.example.playlistmaker.presentation.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.presentation.models.PlaylistInfo

interface PlaylistItemClickListener {
    fun onClick(track: PlaylistInfo)
}

class PlaylistAdapter(
    private val onItemClickListener: PlaylistItemClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    var items: MutableList<PlaylistInfo> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(items[position])
        }
    }

    override fun getItemCount() = items.size
}