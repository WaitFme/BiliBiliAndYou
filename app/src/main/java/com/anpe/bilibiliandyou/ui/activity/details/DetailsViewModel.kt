package com.anpe.bilibiliandyou.ui.activity.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anpe.bilibiliandyou.entity.dynamic.DynamicEntity
import com.anpe.bilibiliandyou.entity.playUrl.PlayUrlEntity
import com.anpe.bilibiliandyou.entity.related.RelatedEntity
import com.anpe.bilibiliandyou.entity.userCard.UserCardEntity
import com.anpe.bilibiliandyou.entity.view.ViewEntity
import com.anpe.bilibiliandyou.network.data.repository.NetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) :AndroidViewModel(application) {
    private val repository = NetRepository(application.applicationContext)

    private val _view = MutableStateFlow(ViewEntity())
    val view: StateFlow<ViewEntity> = _view

    fun getView(aid: Long) {
        viewModelScope.launch {
            _view.emit(repository.getView(aid))
        }
    }

    private val _playUrl = MutableStateFlow(PlayUrlEntity())
    val playUrl: StateFlow<PlayUrlEntity> = _playUrl

    fun getPlayUrl(aid: Long, cid: Long) {
        viewModelScope.launch {
            _playUrl.emit(repository.getPlayUrl(aid, cid))
        }
    }

    private val _region = MutableStateFlow(DynamicEntity())
    val region: StateFlow<DynamicEntity> = _region

    fun getRegionVideo() {
        viewModelScope.launch {
            _region.emit(repository.getDynamic(1, 1, 100))
        }
    }

    private val _related = MutableStateFlow(RelatedEntity())
    val related: StateFlow<RelatedEntity> = _related

    fun getRelatedInfo(aid: Long) {
        viewModelScope.launch {
            _related.emit(repository.getRelated(aid))
        }
    }

    private val _userCard = MutableStateFlow(UserCardEntity())
    val userCard: StateFlow<UserCardEntity> = _userCard

    fun refreshUserCard(mid: String) {
        viewModelScope.launch {
            _userCard.emit(repository.getUserCard(mid))
        }
    }
}