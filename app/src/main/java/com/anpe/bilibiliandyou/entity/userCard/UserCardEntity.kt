package com.anpe.bilibiliandyou.entity.userCard

data class UserCardEntity(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null,
    val ttl: Int? = null
)

data class Data(
    val archive_count: Int,
    val article_count: Int,
    val card: Card,
    val follower: Int,
    val following: Boolean,
    val like_num: Int
)

data class Card(
    val DisplayRank: String,
    val Official: Official,
    val approve: Boolean,
    val article: Int,
    val attention: Int,
    val attentions: List<Any>,
    val birthday: String,
    val description: String,
    val face: String,
    val face_nft: Int,
    val face_nft_type: Int,
    val fans: Int,
    val friend: Int,
    val is_senior_member: Int,
    val level_info: LevelInfo,
    val mid: String,
    val name: String,
    val nameplate: Nameplate,
    val official_verify: OfficialVerify,
    val pendant: Pendant,
    val place: String,
    val rank: String,
    val regtime: Int,
    val sex: String,
    val sign: String,
    val spacesta: Int,
    val vip: Vip
)

data class Official(
    val desc: String,
    val role: Int,
    val title: String,
    val type: Int
)

data class LevelInfo(
    val current_exp: Int,
    val current_level: Int,
    val current_min: Int,
    val next_exp: Int
)

data class Nameplate(
    val condition: String,
    val image: String,
    val image_small: String,
    val level: String,
    val name: String,
    val nid: Int
)

data class OfficialVerify(
    val desc: String,
    val type: Int
)

data class Pendant(
    val expire: Int,
    val image: String,
    val image_enhance: String,
    val image_enhance_frame: String,
    val name: String,
    val pid: Int
)

data class Vip(
    val avatar_subscript: Int,
    val avatar_subscript_url: String,
    val due_date: Long,
    val label: Label,
    val nickname_color: String,
    val role: Int,
    val status: Int,
    val theme_type: Int,
    val tv_due_date: Int,
    val tv_vip_pay_type: Int,
    val tv_vip_status: Int,
    val type: Int,
    val vipStatus: Int,
    val vipType: Int,
    val vip_pay_type: Int
)

data class Label(
    val bg_color: String,
    val bg_style: Int,
    val border_color: String,
    val img_label_uri_hans: String,
    val img_label_uri_hans_static: String,
    val img_label_uri_hant: String,
    val img_label_uri_hant_static: String,
    val label_theme: String,
    val path: String,
    val text: String,
    val text_color: String,
    val use_img_label: Boolean
)