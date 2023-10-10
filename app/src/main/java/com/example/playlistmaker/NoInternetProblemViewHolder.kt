package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class NoInternetProblemViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.activity_search_no_internet, parent, false)
) {

    val retryButton: Button = itemView.findViewById(R.id.retry_search_button)

    fun bind(problemState: NoInternetProblem) {
    }
}