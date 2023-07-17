package com.anpe.bilibiliandyou.network.data.state

import com.anpe.bilibiliandyou.network.data.model.hotWord.HotWordEntity

sealed class HotWordState {
    object Idle: HotWordState()

    object Loading: HotWordState()

    data class Success(val hotWordEntity: HotWordEntity): HotWordState()

    data class Error(val error: String): HotWordState()
}