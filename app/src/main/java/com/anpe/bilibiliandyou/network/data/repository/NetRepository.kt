package com.anpe.bilibiliandyou.network.data.repository

import android.content.Context
import com.anpe.bilibiliandyou.network.service.LoginService
import com.anpe.bilibiliandyou.network.service.NetService
import com.anpe.bilibiliandyou.network.service.ApiService

class NetRepository(context: Context) {
    private val serviceNet = NetService.getService(context)
    private val serviceLogin = LoginService.getService(context)
    private val serviceApi = ApiService.getService(context)

    fun getBilibiliCookie() = serviceApi.getBilibiliCookie()

    fun getSuggestTest(term: String) = serviceApi.getSuggestTest(term)

    suspend fun getQrcode() = serviceLogin.generateQrcode()

    suspend fun getQrcodeStatus(key: String) = serviceLogin.pollQrcode(key)

    suspend fun getDynamic(rid: Int, pager: Int, item: Int) = serviceNet.getDynamicVideo(pager, item, rid)

    suspend fun getHotVideo(pager: Int = 1, item: Int = 20) = serviceNet.getHotVideo(pager, item)

    suspend fun getIndexVideo(item: Int = 20, fresh: Int = 1, version: String = "V3") = serviceNet.getIndexVideo(item, fresh, version)

    suspend fun getRelated(aid: Long) = serviceNet.getRelated(aid)

    suspend fun getView(aid: Long) = serviceNet.getView(aid)

    suspend fun getUserCard(mid: String) = serviceNet.getUserCard(mid)

    suspend fun getPlayUrl(aid: Long, cid: Long, qn: Int = 32) = serviceNet.getPlayUrl(aid = aid, cid = cid, qn = qn)

//    suspend fun getSuggest(term: String) = serviceNet.getSearchSuggest(term).toSuggestRootEntity()

    suspend fun getSearchResult(keyword: String) = serviceNet.getSearchResult(keyword)

    suspend fun getHotWord() = serviceNet.getHotWord()
}