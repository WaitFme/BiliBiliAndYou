package com.anpe.bilibiliandyou.network.data.model.hotWord

import com.google.gson.annotations.SerializedName


data class HotWordEntity(
    @SerializedName("code")
    val code: Int,
    @SerializedName("cost")
    val cost: Cost,
    @SerializedName("exp_str")
    val expStr: String,
    @SerializedName("hotword_egg_info")
    val hotwordEggInfo: String,
    @SerializedName("list")
    val list: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("seid")
    val seid: String,
    @SerializedName("timestamp")
    val timestamp: Int
)

data class Cost(
    @SerializedName("deserialize_response")
    val deserializeResponse: String,
    @SerializedName("hotword_request")
    val hotwordRequest: String,
    @SerializedName("hotword_request_format")
    val hotwordRequestFormat: String,
    @SerializedName("hotword_response_format")
    val hotwordResponseFormat: String,
    @SerializedName("main_handler")
    val mainHandler: String,
    @SerializedName("params_check")
    val paramsCheck: String,
    @SerializedName("total")
    val total: String
)

data class Data(
    @SerializedName("call_reason")
    val callReason: Int,
    @SerializedName("goto_type")
    val gotoType: Int,
    @SerializedName("goto_value")
    val gotoValue: String,
    @SerializedName("heat_layer")
    val heatLayer: String,
    @SerializedName("hot_id")
    val hotId: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("live_id")
    val liveId: List<Int>,
    @SerializedName("name_type")
    val nameType: String,
    @SerializedName("pos")
    val pos: Int,
    @SerializedName("resource_id")
    val resourceId: Int,
    @SerializedName("score")
    val score: Double,
    @SerializedName("show_name")
    val showName: String,
    @SerializedName("stat_datas")
    val statDatas: StatDatas,
    @SerializedName("status")
    val status: String,
    @SerializedName("word_type")
    val wordType: Int
)

data class StatDatas(
    @SerializedName("card_type")
    val cardType: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("danmu_1h")
    val danmu1h: String,
    @SerializedName("danmu_rt")
    val danmuRt: String,
    @SerializedName("etime")
    val etime: String,
    @SerializedName("is_commercial")
    val isCommercial: String,
    @SerializedName("likes_1h")
    val likes1h: String,
    @SerializedName("likes_rt")
    val likesRt: String,
    @SerializedName("mtime")
    val mtime: String,
    @SerializedName("play_1h")
    val play1h: String,
    @SerializedName("play_rt")
    val playRt: String,
    @SerializedName("play_total_rank_1h_div")
    val playTotalRank1hDiv: String,
    @SerializedName("pos_end")
    val posEnd: String,
    @SerializedName("pos_start")
    val posStart: String,
    @SerializedName("pos_type")
    val posType: String,
    @SerializedName("rankscore")
    val rankscore: String,
    @SerializedName("related_resource")
    val relatedResource: String,
    @SerializedName("reply_1h")
    val reply1h: String,
    @SerializedName("reply_rt")
    val replyRt: String,
    @SerializedName("score_exp")
    val scoreExp: String,
    @SerializedName("share_1h")
    val share1h: String,
    @SerializedName("share_rt")
    val shareRt: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("stime")
    val stime: String
)