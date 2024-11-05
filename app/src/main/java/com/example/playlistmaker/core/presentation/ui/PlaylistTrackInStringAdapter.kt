package com.example.playlistmaker.core.presentation.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.core.domain.models.Track

interface TrackClickListener {
    fun onClick(track: Track)
}

interface TrackLongClickListener {
    fun onLongClick(track: Track): Boolean
}

object TrackDiffCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}

class PlaylistTrackInStringAdapter(
    private val onItemClickListener: TrackClickListener,
    private val onLongClickListener: TrackLongClickListener
) : ListAdapter<Track, PlaylistTrackInStringViewHolder>(TrackDiffCallback) {

    var items: MutableList<Track> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistTrackInStringViewHolder {
        return PlaylistTrackInStringViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistTrackInStringViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(items[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onLongClick(items[position])
        }
    }

    override fun getItemCount() = items.size
}