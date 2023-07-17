package com.anpe.bilibiliandyou.network.service

import android.content.Context
import com.anpe.bilibiliandyou.entity.login.GenerateQrcodeEntity
import com.anpe.bilibiliandyou.entity.login.PollQrcode
import com.anpe.bilibiliandyou.network.cookieManager.CookieManger
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface LoginService {
    companion object {
        private const val PASSPORT_URL = "https://passport.bilibili.com/"

        private var service: LoginService? = null

        fun getService(context: Context): LoginService {
            if (null == service) {
                val client = OkHttpClient.Builder()
                    .cookieJar(CookieManger(context))
                    .callTimeout(30, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(PASSPORT_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                service = retrofit.create(LoginService::class.java)
            }

            return service!!
        }
    }

    @GET("x/passport-login/web/qrcode/generate")
    suspend fun generateQrcode(): GenerateQrcodeEntity

    @GET("x/passport-login/web/qrcode/poll")
    suspend fun pollQrcode(@Query("qrcode_key") key: String): PollQrcode
}