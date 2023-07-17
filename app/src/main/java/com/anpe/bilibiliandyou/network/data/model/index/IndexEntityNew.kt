package com.anpe.bilibiliandyou.network.data.model.index

import com.google.gson.annotations.SerializedName


data class IndexEntityNew(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
)

data class Data(
    @SerializedName("business_card")
    val businessCard: Any,
    @SerializedName("floor_info")
    val floorInfo: Any,
    @SerializedName("item")
    val item: List<Item>,
    @SerializedName("mid")
    val mid: Int,
    @SerializedName("preload_expose_pct")
    val preloadExposePct: Double,
    @SerializedName("preload_floor_expose_pct")
    val preloadFloorExposePct: Double,
    @SerializedName("side_bar_column")
    val sideBarColumn: List<SideBarColumn>,
    @SerializedName("user_feature")
    val userFeature: Any
)

data class Item(
    @SerializedName("av_feature")
    val avFeature: Any,
    @SerializedName("business_info")
    val businessInfo: Any,
    @SerializedName("bvid")
    val bvid: String,
    @SerializedName("cid")
    val cid: Int,
    @SerializedName("comic_detail_info")
    val comicDetailInfo: Any,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("enable_vt")
    val enableVt: Int,
    @SerializedName("game_detail_info")
    val gameDetailInfo: Any,
    @SerializedName("goto")
    val goto: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_followed")
    val isFollowed: Int,
    @SerializedName("is_stock")
    val isStock: Int,
    @SerializedName("ogv_info")
    val ogvInfo: Any,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("pos")
    val pos: Int,
    @SerializedName("pubdate")
    val pubdate: Int,
    @SerializedName("rcmd_reason")
    val rcmdReason: RcmdReason,
    @SerializedName("room_info")
    val roomInfo: Any,
    @SerializedName("show_info")
    val showInfo: Int,
    @SerializedName("stat")
    val stat: Stat,
    @SerializedName("title")
    val title: String,
    @SerializedName("track_id")
    val trackId: String,
    @SerializedName("uri")
    val uri: String
)

data class SideBarColumn(
    @SerializedName("av_feature")
    val avFeature: Any,
    @SerializedName("card_type")
    val cardType: String,
    @SerializedName("card_type_en")
    val cardTypeEn: String,
    @SerializedName("comic")
    val comic: Any,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("enable_vt")
    val enableVt: Int,
    @SerializedName("goto")
    val goto: String,
    @SerializedName("horizontal_cover_16_10")
    val horizontalCover1610: String,
    @SerializedName("horizontal_cover_16_9")
    val horizontalCover169: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_finish")
    val isFinish: Int,
    @SerializedName("is_play")
    val isPlay: Int,
    @SerializedName("is_rec")
    val isRec: Int,
    @SerializedName("is_started")
    val isStarted: Int,
    @SerializedName("new_ep")
    val newEp: NewEp,
    @SerializedName("pos")
    val pos: Int,
    @SerializedName("producer")
    val producer: List<Producer>,
    @SerializedName("source")
    val source: String,
    @SerializedName("stats")
    val stats: Stats,
    @SerializedName("styles")
    val styles: List<String>,
    @SerializedName("sub_title")
    val subTitle: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("track_id")
    val trackId: String,
    @SerializedName("url")
    val url: String
)

data class Owner(
    @SerializedName("face")
    val face: String,
    @SerializedName("mid")
    val mid: Long,
    @SerializedName("name")
    val name: String
)

data class RcmdReason(
    @SerializedName("content")
    val content: String,
    @SerializedName("reason_type")
    val reasonType: Int
)

data class Stat(
    @SerializedName("danmaku")
    val danmaku: Int,
    @SerializedName("like")
    val like: Int,
    @SerializedName("view")
    val view: Int,
    @SerializedName("vt")
    val vt: Int
)

data class NewEp(
    @SerializedName("cover")
    val cover: String,
    @SerializedName("day_of_week")
    val dayOfWeek: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("index_show")
    val indexShow: String,
    @SerializedName("long_title")
    val longTitle: String,
    @SerializedName("pub_time")
    val pubTime: String,
    @SerializedName("title")
    val title: String
)

data class Producer(
    @SerializedName("is_contribute")
    val isContribute: Int,
    @SerializedName("mid")
    val mid: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: Int
)

data class Stats(
    @SerializedName("coin")
    val coin: Int,
    @SerializedName("danmaku")
    val danmaku: Int,
    @SerializedName("favorite")
    val favorite: Int,
    @SerializedName("follow")
    val follow: Int,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("reply")
    val reply: Int,
    @SerializedName("series_follow")
    val seriesFollow: Int,
    @SerializedName("series_view")
    val seriesView: Int,
    @SerializedName("view")
    val view: Int
)