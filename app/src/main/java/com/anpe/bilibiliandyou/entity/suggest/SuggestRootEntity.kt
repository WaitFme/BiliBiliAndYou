package com.anpe.bilibiliandyou.entity.suggest

import com.anpe.bilibiliandyou.network.data.model.hotWord.Data

data class SuggestRootEntity(
    val data: List<Data>? = null
)

data class Data (
    val name: String,
    val ref: Int,
    val spid: Int,
    val term: String,
    val value: String
)
