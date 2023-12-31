package com.anpe.bilibiliandyou.network.cookieManager

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

open class PersistentCookieStore(context: Context) {
    private val TAG = "PersistentCookieStore"
    private val COOKIE_PREFS = "Cookies_Prefs"

    private val cookies: MutableMap<String, ConcurrentHashMap<String, Cookie>> = hashMapOf()
    private val cookiePrefs: SharedPreferences

    init {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0)

        // 将持久化的cookies缓存到内存中 即map cookies
        val prefsMap = cookiePrefs.all
        for ((key, value) in prefsMap) {
            val cookieNames = TextUtils.split(value as String, ",")
            for (name in cookieNames) {
                Log.d(TAG, "names: $name")
                val encodedCookie = cookiePrefs.getString(name, null)
                encodedCookie?.apply {
                    val decodeCookie = this.decodeCookie()
                    decodeCookie?.apply {
                        if (!cookies.containsKey(key)) {
                            cookies[key] = ConcurrentHashMap()
                        }
                        cookies[key]?.set(name, this)
                    }
                }
            }
        }
    }

    private fun Cookie.getCookieToken() = "${this.name}@${this.domain}"

    fun add(url: HttpUrl, cookie: Cookie) {
        val name = cookie.getCookieToken()

        // 将cookies缓存到内存中 如果缓存过期 就重置此cookie
        if (!cookie.persistent) {
            if (!cookies.containsKey(url.host)) {
                cookies[url.host] = ConcurrentHashMap<String, Cookie>()
            }
            cookies[url.host]!![name] = cookie
        } else {
            if (cookies.containsKey(url.host)) {
                cookies[url.host]!!.remove(name)
            }
        }

        // 将cookies持久化到本地
        val prefsWriter = cookiePrefs.edit()

        // 问题处
        cookiePrefs.getString(url.host, null).apply {
            if (this == null) {
                prefsWriter.putString(url.host, name)
            } else {
                val list = this.split(",")
                if (name !in list) {
                    val content = "$this,$name"
                    prefsWriter.putString(url.host, content)
                }
            }
        }
//        prefsWriter.putString(url.host, TextUtils.join(",", cookies[url.host]!!.keySet(cookie)))
        prefsWriter.putString(name, OkHttpCookies(cookie).encodeCookie())
        prefsWriter.apply()
    }

    operator fun get(url: HttpUrl): List<Cookie> {
        val ret = ArrayList<Cookie>()
        val list = url.host.split(".").reversed()
        val host = "${list[1]}.${list[0]}"
        if (cookies.containsKey(host)) {
            ret.addAll(cookies[host]!!.values)
        }
        return ret
    }

    fun removeAll(): Boolean {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookies.clear()
        return true
    }

    fun remove(url: HttpUrl, cookie: Cookie): Boolean {
        val name = cookie.getCookieToken()
        return if (cookies.containsKey(url.host) && cookies[url.host]!!.containsKey(name)) {
            cookies[url.host]!!.remove(name)
            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(name)) {
                prefsWriter.remove(name)
            }
            prefsWriter.putString(url.host, TextUtils.join(",", cookies[url.host]!!.keySet(cookie)))
            prefsWriter.apply()
            true
        } else {
            false
        }
    }

    fun getCookies(): List<Cookie> {
        val ret = ArrayList<Cookie>()
        for (key in cookies.keys) {
            ret.addAll(cookies[key]!!.values)
        }
        return ret
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    private fun OkHttpCookies.encodeCookie(): String? {
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(this)
        } catch (e: IOException) {
            Log.d(TAG, "IOException in encodeCookie", e)
            return null
        }
        return os.toByteArray().byteArrayToHexString()
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    private fun String.decodeCookie(): Cookie? {
        val bytes = this.hexStringToByteArray()
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        var cookie: Cookie? = null
        try {
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            cookie = (objectInputStream.readObject() as OkHttpCookies).getCookies()
        } catch (e: IOException) {
            Log.d(TAG, "IOException in decodeCookie", e)
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "ClassNotFoundException in decodeCookie", e)
        }
        return cookie
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private fun ByteArray.byteArrayToHexString(): String {
        val sb = StringBuilder(size * 2)
        for (element in this) {
            val v = element.toInt() and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().uppercase(Locale.US)
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private fun String.hexStringToByteArray(): ByteArray {
        val data = ByteArray(length / 2)
        for (index in indices step 2) {
            data[index / 2] = (
                    (Character.digit(this[index], 16) shl 4) +
                            Character.digit(this[index + 1], 16)
                    ).toByte()
        }
        return data
    }
}