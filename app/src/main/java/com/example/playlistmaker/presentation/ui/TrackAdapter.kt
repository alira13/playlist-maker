package com.example.playlistmaker.presentation.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track

interface ItemClickListener {
    fun onClick(track: Track)
}

class TrackAdapter(
    private val onItemClickListener: ItemClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    var items: MutableList<Track> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(items[position])
        }
    }

    override fun getItemCount() = items.size
}