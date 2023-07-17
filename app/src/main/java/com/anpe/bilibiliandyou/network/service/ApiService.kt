package com.anpe.bilibiliandyou.network.service

import android.content.Context
import com.anpe.bilibiliandyou.constant.Constant
import com.anpe.bilibiliandyou.network.cookieManager.CookieManger
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiService {
    companion object {
        private const val BASE_URL = "https://bilibili.com"
        private const val PASSPORT_BASE_URL = "https://passport.bilibili.com"
        private const val SEARCH_BASE_URL = "https://s.search.bilibili.com"
        private const val API_BASE_URL = "https://api.bilibili.com"

        private var service: ApiService? = null

        fun getService(context: Context): ApiService {
            if (service == null) {
                val client = OkHttpClient.Builder()
                    .cookieJar(CookieManger(context))
                    .callTimeout(5, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(client)
                    .build()

                service = retrofit.create(ApiService::class.java)
            }

            return service as ApiService
        }
    }

    @Headers(BASE_URL)
    @GET("/")
    fun getBilibiliCookie(
        @Header("Host") host: String = "www.bilibili.com",
        @Header("User-Agent") agent: String = Constant.BROWSER_UA
    ): Call<ResponseBody>

    @Headers(SEARCH_BASE_URL)
    @GET("/main/suggest")
    fun getSuggestTest(
        @Query("term")term: String,
        @Query("main_ver") version: String = "V2"
    ): Call<ResponseBody>
}