package com.anpe.bilibiliandyou.network.service

import android.content.Context
import com.anpe.bilibiliandyou.network.cookieManager.CookieManger
import com.anpe.bilibiliandyou.entity.HotVedio.HotVideoEntity
import com.anpe.bilibiliandyou.entity.dynamic.DynamicEntity
import com.anpe.bilibiliandyou.network.data.model.hotWord.HotWordEntity
import com.anpe.bilibiliandyou.entity.login.GenerateQrcodeEntity
import com.anpe.bilibiliandyou.entity.login.PollQrcode
import com.anpe.bilibiliandyou.entity.playUrl.PlayUrlEntity
import com.anpe.bilibiliandyou.entity.related.RelatedEntity
import com.anpe.bilibiliandyou.entity.suggest.SuggestEntity
import com.anpe.bilibiliandyou.entity.userCard.UserCardEntity
import com.anpe.bilibiliandyou.entity.view.ViewEntity
import com.anpe.bilibiliandyou.network.data.model.index.IndexEntityNew
import com.anpe.bilibiliandyou.network.data.model.search.SearchEntityNew
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface NetService {
    companion object {
        private const val PASSPORT_BASE_URL = "https://passport.bilibili.com"
        private const val SEARCH_BASE_URL = "https://s.search.bilibili.com"
        private const val API_BASE_URL = "https://api.bilibili.com"

        private var service: NetService? = null

        fun getService(context: Context): NetService {
            if (service == null) {
                val client = OkHttpClient.Builder()
                    .callTimeout(5, TimeUnit.SECONDS)
                    .cookieJar(CookieManger(context))
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                service = retrofit.create(NetService::class.java)
            }

            return service as NetService
        }
    }

    // 获取登陆二维码信息
    @Headers(PASSPORT_BASE_URL)
    @GET("/x/passport-login/web/qrcode/generate")
    suspend fun getGenerateQrcode(): GenerateQrcodeEntity

    // 获取登陆二维码状态
    @Headers(PASSPORT_BASE_URL)
    @GET("/x/passport-login/web/qrcode/poll")
    suspend fun getPollQrcode(@Query("qrcode_key") key: String): PollQrcode

    // 分区最新视频
    @Headers(API_BASE_URL)
    @GET("/x/web-interface/dynamic/region")
    suspend fun getDynamicVideo(@Query("pn") pager: Int, @Query("ps") item: Int, @Query("rid") rid: Int = 1): DynamicEntity

    // 首页推荐视频
    @Headers(API_BASE_URL)
    @GET("/x/web-interface/index/top/feed/rcmd")
    suspend fun getIndexVideo(@Query("ps") item: Int, @Query("fresh_idx") fresh: Int,  @Query("feed_version") version: String): IndexEntityNew

    // 热门视频
    @Headers(API_BASE_URL)
    @GET("/x/web-interface/popular")
    suspend fun getHotVideo(@Query("pn") pager: Int, @Query("ps") item: Int): HotVideoEntity

    // 单视频推荐列表
    @Headers(API_BASE_URL)
    @GET("/x/web-interface/archive/related")
    suspend fun getRelated(@Query("aid") aid: Long): RelatedEntity

    // 视频详细信息
    @Headers(API_BASE_URL)
    @GET("/x/web-interface/view")
    suspend fun getView(@Query("aid") aid: Long): ViewEntity

    // 用户名片
    @Headers(API_BASE_URL)
    @GET("/x/web-interface/card")
    suspend fun getUserCard(@Query("mid") mid: String): UserCardEntity

    // 播放链接
    @Headers(API_BASE_URL)
    @GET("/x/player/playurl")
    suspend fun getPlayUrl(
        @Query("avid") aid: Long,
        @Query("cid") cid: Long,
        @Query("qn") qn: Int,
        @Query("fnval") fnval: Int = 1,
        @Query("fnver") fnver: Int = 0,
        @Query("fourk") fourk: Int = 0,
    ): PlayUrlEntity

    // 评论
    @Headers(API_BASE_URL)
    @GET("/x/v2/reply/main")
    suspend fun getReply(
        @Query("oid") oid: Long,
        @Query("type") type: Int = 1,
        @Query("mode") mode: Int = 0,
        @Query("next") next: Int? = null,
        @Query("ps") item: Int = 30
    ): Call<ResponseBody>

    // 搜索
    @Headers(API_BASE_URL)
    @GET("/x/web-interface/search/all/v2")
    suspend fun getSearchResult(@Query("keyword") keyword: String): SearchEntityNew

    // 搜索建议
    @Headers(SEARCH_BASE_URL)
    @GET("/main/suggest")
    suspend fun getSearchSuggest(@Query("term")term: String): SuggestEntity

    @Headers(SEARCH_BASE_URL)
    @GET("/main/hotword")
    suspend fun getHotWord(): HotWordEntity
}