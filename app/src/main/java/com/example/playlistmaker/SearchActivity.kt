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

class SearchActivity : AppCompatActivity(), OnRetryButtonClickListener {
    private var searchValue: String = ""

    private lateinit var emptyListProblemText: TextView
    private lateinit var searchNoInternetProblemText: TextView
    private lateinit var retrySearchButton: Button
    private lateinit var inputEditText: EditText

    private val baseUrl: String = "https://itunes.apple.com/"
    private val trackApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrackApiService::class.java)

    private val trackAdapter = TrackAdapter(this)

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.arrow_back_button)
        backButton.setOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clear_button)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            clearButton.visibility = View.GONE
        }

        val trackListRecyclerView: RecyclerView = findViewById(R.id.track_list)
        trackListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }

        inputEditText = findViewById(R.id.input_edit_text)
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchValue = s.toString()
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

        emptyListProblemText = findViewById(R.id.empty_list_problem_text)
        searchNoInternetProblemText = findViewById(R.id.search_no_internet_problem_text)
        retrySearchButton = findViewById((R.id.retry_search_button))

        retrySearchButton.setOnClickListener {
            putRequest()
        }
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

    private fun putRequest() {
        emptyListProblemText.visibility = View.GONE
        searchNoInternetProblemText.visibility = View.GONE
        retrySearchButton.visibility = View.GONE

        if (inputEditText.text.isNotEmpty()) {
            trackApiService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {

                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.isSuccessful) {
                        trackAdapter.items = response.body()!!.results.toMutableList()
                        if (trackAdapter.items.isNotEmpty()) {
                            Log.d("MY_LOG", "Successful: ${trackAdapter.items}")
                        } else {
                            trackAdapter.items.clear()
                            trackAdapter.notifyDataSetChanged()
                            emptyListProblemText.visibility= View.VISIBLE
                        }
                    } else {
                        Log.d("MY_LOG", "notSuccessful: ${trackAdapter.items}")
                        trackAdapter.items.clear()
                        trackAdapter.notifyDataSetChanged()
                        emptyListProblemText.visibility= View.VISIBLE
                        Log.d("MY_LOG", "notSuccessful: done")
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    Log.d("MY_LOG", "OnFailure: ${trackAdapter.items}")
                    trackAdapter.items.clear()
                    trackAdapter.notifyDataSetChanged()
                    searchNoInternetProblemText.visibility=View.VISIBLE
                    retrySearchButton.visibility = View.VISIBLE
                    Log.d("MY_LOG", "OnFailure: done")
                }
            })
        }
    }

    override fun onRetryButtonClick() {
        putRequest()
    }

    private companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }
}
