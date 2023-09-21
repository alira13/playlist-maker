package com.example.playlistmaker
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface OnRetryButtonClickListener {
    fun onRetryButtonClick()
}

class SearchResultAdapter(
    val onRetryButtonClickListener: OnRetryButtonClickListener
) : RecyclerView.Adapter<ViewHolder>() {

    var items: MutableList<SearchResult> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        const val VIEW_TYPE_TRACK_LIST = 1
        const val VIEW_TYPE_NO_INTERNET_PROBLEM_STATE = 2
        const val VIEW_TYPE_EMPTY_LIST_PROBLEM_STATE = 3
        const val VIEW_TYPE_UNKNOWN = 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Track -> {
                VIEW_TYPE_TRACK_LIST
            }

            is NoInternetProblem -> {
                VIEW_TYPE_NO_INTERNET_PROBLEM_STATE
            }

            is EmptyListProblem -> {
                VIEW_TYPE_EMPTY_LIST_PROBLEM_STATE
            }

            else -> {
                VIEW_TYPE_UNKNOWN
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TRACK_LIST -> TrackViewHolder(parent)
            VIEW_TYPE_NO_INTERNET_PROBLEM_STATE -> NoInternetProblemViewHolder(parent)
            VIEW_TYPE_EMPTY_LIST_PROBLEM_STATE -> EmptyListProblemViewHolder(parent)
            else -> throw IllegalStateException("There is no ViewHolder for viewType=$viewType")
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Track -> {
                (holder as TrackViewHolder).bind(item)
            }

            is NoInternetProblem -> {
                (holder as NoInternetProblemViewHolder).apply {
                    bind(item)
                    retryButton.setOnClickListener {
                        onRetryButtonClickListener.onRetryButtonClick()
                    }
                }
            }

            is EmptyListProblem -> {
                (holder as EmptyListProblemViewHolder).bind(item)
            }

            else -> {
                // nothing
            }
        }
    }
    override fun getItemCount()= items.size
}