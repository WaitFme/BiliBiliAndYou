package com.anpe.bilibiliandyou.network.data.intent

sealed class MainIntent {
    // 首页
    object GetIndex: MainIntent()

    // 动态
    object GetHotWord: MainIntent()

    // 搜索
    class GetSearchResult(val keyWord: String): MainIntent()

    // 搜索建议
    class GetSearchSuggest(val term: String): MainIntent()
}