package com.anpe.bilibiliandyou.network.cookieManager

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieManger(context: Context): CookieJar {
    private val TAG = "PersistentCookieStore"
    private val cookieStore = PersistentCookieStore(context)

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url]
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isNotEmpty()) {
            for (item in cookies) {
                cookieStore.add(url, item)
            }
        }
    }
}