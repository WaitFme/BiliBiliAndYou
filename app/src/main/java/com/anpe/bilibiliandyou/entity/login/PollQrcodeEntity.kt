package com.anpe.bilibiliandyou.entity.login

data class PollQrcode(
    val code: Int? = null,
    val `data`: PollData? = null,
    val message: String? = null,
    val ttl: Int? = null
)

data class PollData(
    val code: Int,
    val message: String,
    val refresh_token: String,
    val timestamp: Int,
    val url: String
)