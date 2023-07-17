package com.anpe.bilibiliandyou.entity.related

data class RelatedEntity(
    val code: Int? = null,
    val `data`: List<Data>? = null,
    val message: String? = null
)

data class Data(
    val aid: Int,
    val bvid: String,
    val cid: Int,
    val copyright: Int,
    val ctime: Int,
    val desc: String,
    val dimension: Dimension,
    val duration: Int,
    val `dynamic`: String,
    val first_frame: String,
    val is_ogv: Boolean,
    val mission_id: Int,
    val ogv_info: Any,
    val owner: Owner,
    val pic: String,
    val pub_location: String,
    val pubdate: Int,
    val rcmd_reason: String,
    val rights: Rights,
    val season_id: Int,
    val season_type: Int,
    val short_link_v2: String,
    val stat: Stat,
    val state: Int,
    val tid: Int,
    val title: String,
    val tname: String,
    val up_from_v2: Int,
    val videos: Int
)

data class Dimension(
    val height: Int,
    val rotate: Int,
    val width: Int
)

data class Owner(
    val face: String,
    val mid: Long,
    val name: String
)

data class Rights(
    val arc_pay: Int,
    val autoplay: Int,
    val bp: Int,
    val download: Int,
    val elec: Int,
    val hd5: Int,
    val is_cooperation: Int,
    val movie: Int,
    val no_background: Int,
    val no_reprint: Int,
    val pay: Int,
    val pay_free_watch: Int,
    val ugc_pay: Int,
    val ugc_pay_preview: Int
)

data class Stat(
    val aid: Int,
    val coin: Int,
    val danmaku: Int,
    val dislike: Int,
    val favorite: Int,
    val his_rank: Int,
    val like: Int,
    val now_rank: Int,
    val reply: Int,
    val share: Int,
    val view: Int,
    val vt: Int,
    val vv: Int
)