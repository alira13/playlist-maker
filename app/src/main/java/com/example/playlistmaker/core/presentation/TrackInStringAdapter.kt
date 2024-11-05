package com.example.playlistmaker.core.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.core.domain.models.Track

class TrackInStringAdapter(
    private val onTrackItemClickListener: TrackItemClickListener
) : RecyclerView.Adapter<TrackInStringViewHolder>() {

    var items: MutableList<Track> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackInStringViewHolder {
        return TrackInStringViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackInStringViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onTrackItemClickListener.onClick(items[position])
        }
    }

    override fun getItemCount() = items.size
}