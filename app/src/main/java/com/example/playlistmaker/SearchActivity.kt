package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), OnRetryButtonClickListener {
    private var searchValue: String = ""
    private companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }

    private lateinit var inputEditText:EditText

    private val baseUrl:String="https://itunes.apple.com/"
    private val trackApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrackApiService::class.java)

    private lateinit var searchResults:List<SearchResult>

    private var searchResultAdapter = SearchResultAdapter(this)

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //region backButton
        val backButton = findViewById<ImageButton>(R.id.arrow_back_button)
        backButton.setOnClickListener {
            finish()
        }
        //endregion backButton

        //region clear button
        val clearButton = findViewById<ImageView>(R.id.clear_button)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            clearButton.visibility=View.GONE
        }
        //endregion clear button

        //region trackList
        val trackListRecyclerView: RecyclerView = findViewById(R.id.track_list)
        trackListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchResultAdapter
        }

        searchResultAdapter.items.addAll(
            getInitialTrackList()
        )
        //endregion trackList

        // region inputEditText
        inputEditText = findViewById(R.id.input_edit_text)
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchValue=s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                    inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                } else {
                    clearButton.visibility = View.VISIBLE
                    inputMethodManager?.showSoftInput(inputEditText, 0)
                }
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                putRequest()
            }
            false
        }
        // endregion inputEditText
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(SEARCH_VALUE, "")
        inputEditText.setText(searchValue)
    }

    private fun getInitialTrackList(): List<Track> {
        return listOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
    }

    private fun putRequest() {
        if (inputEditText.text.isNotEmpty()) {
            trackApiService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {

                    override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>)
                    {
                        if (response.isSuccessful) {
                            if(response.body()!=(null)) {
                                searchResultAdapter.items=response.body()!!.results.toMutableList()
                            }
                            else searchResultAdapter.items = mutableListOf(EmptyListProblem())
                        } else {
                            searchResultAdapter.items.clear()
                            searchResultAdapter.items = mutableListOf(EmptyListProblem())
                            searchResultAdapter.notifyDataSetChanged()
                            Log.d("MY_LOG", "**onResponse: ${searchResultAdapter.items}")
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable)
                    {
                        searchResultAdapter.items.clear()
                        searchResultAdapter.items=mutableListOf(NoInternetProblem())
                        searchResultAdapter.notifyDataSetChanged()
                        Log.d("MY_LOG", "**OnFailure: ${searchResultAdapter.items}")
                    }
                })
        }
    }

    override fun onRetryButtonClick() {
        putRequest()
    }
}
