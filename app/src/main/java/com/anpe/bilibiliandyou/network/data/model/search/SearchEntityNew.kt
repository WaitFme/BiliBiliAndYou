package com.anpe.bilibiliandyou.network.data.model.search
import com.google.gson.annotations.SerializedName


data class SearchEntityNew(
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
    @SerializedName("app_display_option")
    val appDisplayOption: AppDisplayOption,
    @SerializedName("cost_time")
    val costTime: CostTime,
    @SerializedName("egg_hit")
    val eggHit: Int,
    @SerializedName("exp_list")
    val expList: ExpList,
    @SerializedName("in_black_key")
    val inBlackKey: Int,
    @SerializedName("in_white_key")
    val inWhiteKey: Int,
    @SerializedName("is_search_page_grayed")
    val isSearchPageGrayed: Int,
    @SerializedName("numPages")
    val numPages: Int,
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pageinfo")
    val pageinfo: Pageinfo,
    @SerializedName("pagesize")
    val pagesize: Int,
    @SerializedName("result")
    val result: List<Result>,
    @SerializedName("rqt_type")
    val rqtType: String,
    @SerializedName("seid")
    val seid: String,
    @SerializedName("show_column")
    val showColumn: Int,
    @SerializedName("show_module_list")
    val showModuleList: List<String>,
    @SerializedName("suggest_keyword")
    val suggestKeyword: String,
    @SerializedName("top_tlist")
    val topTlist: TopTlist
)

data class AppDisplayOption(
    @SerializedName("is_search_page_grayed")
    val isSearchPageGrayed: Int
)

data class CostTime(
    @SerializedName("as_request")
    val asRequest: String,
    @SerializedName("as_request_format")
    val asRequestFormat: String,
    @SerializedName("as_response_format")
    val asResponseFormat: String,
    @SerializedName("deserialize_response")
    val deserializeResponse: String,
    @SerializedName("illegal_handler")
    val illegalHandler: String,
    @SerializedName("is_risk_query")
    val isRiskQuery: String,
    @SerializedName("main_handler")
    val mainHandler: String,
    @SerializedName("mysql_request")
    val mysqlRequest: String,
    @SerializedName("params_check")
    val paramsCheck: String,
    @SerializedName("total")
    val total: String
)

data class ExpList(
    @SerializedName("5511")
    val x5511: Boolean,
    @SerializedName("6608")
    val x6608: Boolean,
    @SerializedName("7706")
    val x7706: Boolean,
    @SerializedName("9901")
    val x9901: Boolean,
    @SerializedName("9920")
    val x9920: Boolean,
    @SerializedName("9931")
    val x9931: Boolean,
    @SerializedName("9947")
    val x9947: Boolean
)

data class Pageinfo(
    @SerializedName("activity")
    val activity: Activity,
    @SerializedName("article")
    val article: Article,
    @SerializedName("bangumi")
    val bangumi: Bangumi,
    @SerializedName("bili_user")
    val biliUser: BiliUser,
    @SerializedName("live")
    val live: Live,
    @SerializedName("live_all")
    val liveAll: LiveAll,
    @SerializedName("live_master")
    val liveMaster: LiveMaster,
    @SerializedName("live_room")
    val liveRoom: LiveRoom,
    @SerializedName("live_user")
    val liveUser: LiveUser,
    @SerializedName("media_bangumi")
    val mediaBangumi: MediaBangumi,
    @SerializedName("media_ft")
    val mediaFt: MediaFt,
    @SerializedName("movie")
    val movie: Movie,
    @SerializedName("operation_card")
    val operationCard: OperationCard,
    @SerializedName("pgc")
    val pgc: Pgc,
    @SerializedName("special")
    val special: Special,
    @SerializedName("topic")
    val topic: Topic,
    @SerializedName("tv")
    val tv: Tv,
    @SerializedName("upuser")
    val upuser: Upuser,
    @SerializedName("user")
    val user: User,
    @SerializedName("video")
    val video: Video
)

data class Result(
    @SerializedName("data")
    val `data`: List<DataX>,
    @SerializedName("result_type")
    val resultType: String
)

data class TopTlist(
    @SerializedName("activity")
    val activity: Int,
    @SerializedName("article")
    val article: Int,
    @SerializedName("bangumi")
    val bangumi: Int,
    @SerializedName("bili_user")
    val biliUser: Int,
    @SerializedName("card")
    val card: Int,
    @SerializedName("live")
    val live: Int,
    @SerializedName("live_master")
    val liveMaster: Int,
    @SerializedName("live_room")
    val liveRoom: Int,
    @SerializedName("live_user")
    val liveUser: Int,
    @SerializedName("media_bangumi")
    val mediaBangumi: Int,
    @SerializedName("media_ft")
    val mediaFt: Int,
    @SerializedName("movie")
    val movie: Int,
    @SerializedName("operation_card")
    val operationCard: Int,
    @SerializedName("pgc")
    val pgc: Int,
    @SerializedName("special")
    val special: Int,
    @SerializedName("topic")
    val topic: Int,
    @SerializedName("tv")
    val tv: Int,
    @SerializedName("upuser")
    val upuser: Int,
    @SerializedName("user")
    val user: Int,
    @SerializedName("video")
    val video: Int
)

data class Activity(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Article(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Bangumi(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class BiliUser(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Live(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class LiveAll(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class LiveMaster(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class LiveRoom(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class LiveUser(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class MediaBangumi(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class MediaFt(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Movie(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class OperationCard(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Pgc(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Special(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Topic(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Tv(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Upuser(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class User(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class Video(
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("total")
    val total: Int
)

data class DataX(
    @SerializedName("aid")
    val aid: Int,
    @SerializedName("arcrank")
    val arcrank: String,
    @SerializedName("arcurl")
    val arcurl: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("badgepay")
    val badgepay: Boolean,
    @SerializedName("biz_data")
    val bizData: Any,
    @SerializedName("bvid")
    val bvid: String,
    @SerializedName("corner")
    val corner: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("danmaku")
    val danmaku: Int,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("enable_vt")
    val enableVt: Int,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("hit_columns")
    val hitColumns: List<String>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_charge_video")
    val isChargeVideo: Int,
    @SerializedName("is_pay")
    val isPay: Int,
    @SerializedName("is_union_video")
    val isUnionVideo: Int,
    @SerializedName("like")
    val like: Int,
    @SerializedName("mid")
    val mid: Int,
    @SerializedName("new_rec_tags")
    val newRecTags: List<Any>,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("play")
    val play: Int,
    @SerializedName("pubdate")
    val pubdate: Int,
    @SerializedName("rank_score")
    val rankScore: Int,
    @SerializedName("rec_reason")
    val recReason: String,
    @SerializedName("rec_tags")
    val recTags: Any,
    @SerializedName("review")
    val review: Int,
    @SerializedName("senddate")
    val senddate: Int,
    @SerializedName("tag")
    val tag: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("typeid")
    val typeid: String,
    @SerializedName("typename")
    val typename: String,
    @SerializedName("upic")
    val upic: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("video_review")
    val videoReview: Int,
    @SerializedName("view_type")
    val viewType: String,
    @SerializedName("vt")
    val vt: Int
)