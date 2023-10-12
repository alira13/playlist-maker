package com.example.playlistmaker
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface OnRetryButtonClickListener {
    fun onRetryButtonClick()
}

class TrackAdapter(
    private val onRetryButtonClickListener: OnRetryButtonClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    var items: MutableList<Track> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }
    
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items[position])
    }
    override fun getItemCount()= items.size
}