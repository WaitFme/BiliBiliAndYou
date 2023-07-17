package com.anpe.bilibiliandyou.network.data.state

import com.anpe.bilibiliandyou.network.data.model.search.SearchEntityNew

sealed class SearchState {
    object Idle: SearchState()

    object Loading: SearchState()

    data class Success(val searchEntity: SearchEntityNew): SearchState()

    data class Error(val error: String): SearchState()
}