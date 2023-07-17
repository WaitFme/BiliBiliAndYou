package com.anpe.bilibiliandyou.entity.HotVedio

import kotlin.collections.List

data class HotVideoEntity(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null,
    val ttl: Int? = null
)

data class Data(
    val list: List<Content>,
    val no_more: Boolean
)