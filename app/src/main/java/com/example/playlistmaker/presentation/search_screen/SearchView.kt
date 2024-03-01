package com.example.playlistmaker.presentation.search_screen

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


interface SearchView:MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: SearchState)
}