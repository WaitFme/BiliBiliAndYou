package com.anpe.bilibiliandyou.network.data.state

import com.anpe.bilibiliandyou.network.data.model.suggest.SuggestEntityTest

sealed class SuggestState {
    object Idle: SuggestState()

    object Loading: SuggestState()

    data class Success(val suggestEntity: SuggestEntityTest): SuggestState()

    data class Failure(val error: String): SuggestState()

    data class Error(val error: String): SuggestState()
}