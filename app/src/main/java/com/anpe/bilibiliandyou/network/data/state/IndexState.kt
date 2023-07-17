package com.anpe.bilibiliandyou.network.data.state

import com.anpe.bilibiliandyou.network.data.model.index.IndexEntityNew


sealed class IndexState {
    object Idle: IndexState()

    object Loading: IndexState()

    data class Success(val indexEntityNew: IndexEntityNew): IndexState()

    data class Failure(val error: String): IndexState()

    data class Error(val error: String): IndexState()
}