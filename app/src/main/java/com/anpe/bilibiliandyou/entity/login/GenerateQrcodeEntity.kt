package com.anpe.bilibiliandyou.entity.login

data class GenerateQrcodeEntity(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null,
    val ttl: Int? = null
)

data class Data(
    val qrcode_key: String,
    val url: String
)