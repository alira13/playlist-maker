package com.example.playlistmaker

import SearchHistory
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), ItemClickListener {
    private var searchValue: String = ""

    private lateinit var emptyListProblemText: TextView
    private lateinit var searchNoInternetProblemText: TextView
    private lateinit var retrySearchButton: Button
    private lateinit var searchTrackView: EditText
    private lateinit var trackListRecyclerView: RecyclerView

    private lateinit var historyText: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var historyTrackListRecyclerView: RecyclerView

    private val baseUrl: String = "https://itunes.apple.com/"
    private val trackApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrackApiService::class.java)

    private val trackAdapter = TrackAdapter(this)
    private val historyTrackAdapter = TrackAdapter(this)
    private lateinit var searchHistory: SearchHistory

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_search)

            val backButton = findViewById<ImageButton>(R.id.arrow_back_button)
            backButton.setOnClickListener {
                finish()
            }

            val clearTrackSearchButton = findViewById<ImageView>(R.id.clear_button)
            clearTrackSearchButton.setOnClickListener {
                searchTrackView.setText("")
                clearTrackSearchButton.visibility = View.GONE
            }

            trackListRecyclerView = findViewById(R.id.track_list)
            trackListRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = trackAdapter
            }

            searchTrackView = findViewById(R.id.input_edit_text)
            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchValue = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (s.isNullOrEmpty()) {
                        clearTrackSearchButton.visibility = View.GONE
                        inputMethodManager?.hideSoftInputFromWindow(searchTrackView.windowToken, 0)
                        //при пустом поле поиска показываем историю, если она не пустая
                        showTrackHistory()
                    } else {
                        //скрываем историю треков как только начинаем печатать что-то в поиске
                        hideTrackHistory()
                        clearTrackSearchButton.visibility = View.VISIBLE
                        inputMethodManager?.showSoftInput(searchTrackView, 0)
                    }
                }
            }

            searchTrackView.addTextChangedListener(simpleTextWatcher)

            searchTrackView.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    putRequest()
                }
                false
            }

            searchTrackView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus
                    && searchTrackView.text.isEmpty()
                    && historyTrackAdapter.items.isNotEmpty()
                ) {
                    historyTrackAdapter.items = searchHistory.getTracks()
                    historyTrackAdapter.notifyDataSetChanged()
                    showTrackHistory()
                } else {
                    showTrackList()
                }
            }

            emptyListProblemText = findViewById(R.id.empty_list_problem_text)
            searchNoInternetProblemText = findViewById(R.id.search_no_internet_problem_text)
            retrySearchButton = findViewById((R.id.retry_search_button))

            retrySearchButton.setOnClickListener {
                putRequest()
            }

            historyTrackListRecyclerView = findViewById(R.id.history_track_list)
            historyTrackListRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = historyTrackAdapter
            }

            searchHistory = SearchHistory(applicationContext as AppSharedPreferences)
            historyTrackAdapter.items = searchHistory.getTracks()

            historyText = findViewById(R.id.history_text)
            clearHistoryButton = findViewById(R.id.clear_history)

            clearHistoryButton.setOnClickListener {
                historyTrackAdapter.clearItems()
                searchHistory.clear()
                hideTrackHistory()
            }

        } catch (ex: Exception) {
            Log.d("MY", ex.message.toString(), ex.fillInStackTrace())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchTrackView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(SEARCH_VALUE, "")
        searchTrackView.setText(searchValue)
    }

    private fun putRequest() {
        hideErrors()

        if (searchTrackView.text.isNotEmpty()) {
            trackApiService.search(searchTrackView.text.toString()).enqueue(object :
                Callback<TrackResponse> {

                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>)
                {
                    if (response.isSuccessful) {
                        trackAdapter.items = response.body()!!.results.toMutableList()
                        if (trackAdapter.items.isNotEmpty()) {
                            showTrackList()
                            Log.d("MY_LOG", "Successful: ${trackAdapter.items}")
                        } else {
                            showEmptyTrackListError()
                        }
                    } else {
                        showEmptyTrackListError()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showConnectionError()
                }
            })
        }
    }

    override fun onClick(track: Track) {
        searchHistory.addTrack(track)
        historyTrackAdapter.notifyDataSetChanged()
    }

    private fun showTrackHistory(){
        hideTrackList()
        hideErrors()
        if(historyTrackAdapter.items.isNotEmpty()) {
            historyText.visibility = View.VISIBLE
            clearHistoryButton.visibility = View.VISIBLE
            historyTrackListRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun hideTrackHistory(){
        historyText.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historyTrackListRecyclerView.visibility = View.GONE
    }

    private fun showConnectionError(){
        trackAdapter.clearItems()
        hideTrackHistory()
        hideTrackList()
        searchNoInternetProblemText.visibility = View.VISIBLE
        retrySearchButton.visibility = View.VISIBLE
    }

    private fun showEmptyTrackListError(){
        trackAdapter.clearItems()
        hideTrackHistory()
        hideTrackList()
        trackListRecyclerView.visibility=View.GONE
        emptyListProblemText.visibility = View.VISIBLE
    }

    private fun hideErrors(){
        emptyListProblemText.visibility = View.GONE
        searchNoInternetProblemText.visibility = View.GONE
        retrySearchButton.visibility = View.GONE
    }

    private fun showTrackList(){
        hideTrackHistory()
        hideErrors()
        trackListRecyclerView.visibility=View.VISIBLE
    }

    private fun hideTrackList(){
        trackListRecyclerView.visibility=View.GONE
    }

    private companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }
}
