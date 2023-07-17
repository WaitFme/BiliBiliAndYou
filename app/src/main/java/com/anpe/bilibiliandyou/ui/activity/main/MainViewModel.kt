package com.anpe.bilibiliandyou.ui.activity.main

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anpe.bilibiliandyou.entity.HotVedio.HotVideoEntity
import com.anpe.bilibiliandyou.entity.dynamic.DynamicEntity
import com.anpe.bilibiliandyou.entity.login.GenerateQrcodeEntity
import com.anpe.bilibiliandyou.entity.login.PollQrcode
import com.anpe.bilibiliandyou.entity.playUrl.PlayUrlEntity
import com.anpe.bilibiliandyou.entity.related.RelatedEntity
import com.anpe.bilibiliandyou.entity.userCard.UserCardEntity
import com.anpe.bilibiliandyou.entity.view.ViewEntity
import com.anpe.bilibiliandyou.network.data.intent.MainIntent
import com.anpe.bilibiliandyou.network.data.model.suggest.SuggestEntityTest
import com.anpe.bilibiliandyou.network.data.repository.NetRepository
import com.anpe.bilibiliandyou.network.data.state.HotWordState
import com.anpe.bilibiliandyou.network.data.state.IndexState
import com.anpe.bilibiliandyou.network.data.state.SearchState
import com.anpe.bilibiliandyou.network.data.state.SuggestState
import com.anpe.bilibiliandyou.network.data.state.SheetState
import com.google.gson.Gson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    val mainIntentChannel = Channel<MainIntent>(Channel.UNLIMITED)

    private val repository = NetRepository(application.applicationContext)

    private var sp: SharedPreferences

    private var freshCount = -1

    private val _hotWordState = MutableStateFlow<HotWordState>(HotWordState.Idle)
    val hotWordState: StateFlow<HotWordState> get() = _hotWordState

    private val _suggestState = MutableStateFlow<SuggestState>(SuggestState.Idle)
    val suggestState: StateFlow<SuggestState> get() = _suggestState

    private val _indexState = MutableStateFlow<IndexState>(IndexState.Idle)
    val indexState: StateFlow<IndexState> = _indexState

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> get() = _searchState

    private val _isStaggeredGrid = MutableStateFlow(false)
    val isStaggeredGrid = _isStaggeredGrid

    init {
        sp = application.getSharedPreferences("config", 0)

        viewModelScope.launch {
            setStaggeredGrid(sp.getBoolean("isStaggeredGrid", false))

            freshCount = sp.getInt("freshCount", 1)

            mainIntentChannel.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.GetIndex -> getIndexVideos()
                    is MainIntent.GetHotWord -> getHotWord()
                    is MainIntent.GetSearchResult -> getSearchResult(it.keyWord)
                    is MainIntent.GetSearchSuggest -> getSuggest(it.term)
                }
            }
        }

        getCookie()

        getIndexVideos()
    }

    private fun getIndexVideos() {
        Log.d(TAG, "getIndexVideos: ")
        viewModelScope.launch {
            _indexState.emit(IndexState.Loading)
            _indexState.value = try {
                freshCount++
                sp.edit().putInt("freshCount", freshCount).apply()
                IndexState.Success(repository.getIndexVideo(item = 28, fresh = freshCount))
            } catch (e: Exception) {
                IndexState.Error(e.localizedMessage ?: "UNKNOWN ERROR")
            }
        }
    }

    private fun getHotWord() {
        viewModelScope.launch {
            _hotWordState.emit(HotWordState.Loading)
            _hotWordState.value = try {
                HotWordState.Success(repository.getHotWord())
            } catch (e: Exception) {
                HotWordState.Error(e.localizedMessage ?: "UNKNOWN ERROR")
            }
        }
    }

    private fun getSuggest(term: String) {
        viewModelScope.launch {
            _suggestState.emit(SuggestState.Loading)
            repository.getSuggestTest(term).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val body = response.body()?.string() as String

                    try {
                        val json = Gson().fromJson(body, SuggestEntityTest::class.java)
                        _suggestState.value = SuggestState.Success(json)
                    } catch (e: Exception) {
                        _suggestState.value = (SuggestState.Error(e.localizedMessage ?: "UNKNOWN ERROR"))
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _suggestState.value = SuggestState.Failure(call.toString())
                }
            })
        }
    }

    private fun getSearchResult(keyword: String) {
        viewModelScope.launch {
            _searchState.emit(SearchState.Loading)
            _searchState.value = try {
                SearchState.Success(repository.getSearchResult(keyword))
            } catch (e: Exception) {
                SearchState.Error(e.localizedMessage ?: "UNKNOWN ERROR")
            }
        }
    }


    private fun getCookie() {
        viewModelScope.launch {
            repository.getBilibiliCookie().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) { }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) { }
            })
        }
    }

    private val _currentId = MutableStateFlow(-1L)
    val currentId: StateFlow<Long> = _currentId
    fun setCurrentId(aid: Long) {
        viewModelScope.launch {
            _currentId.emit(aid)
        }
    }

    /*private val _searchResult = MutableStateFlow(SearchResultEntity())
    val searchResult: StateFlow<SearchResultEntity> = _searchResult
    fun getSearchResult(keyword: String) {
        viewModelScope.launch {
            _searchResult.emit(repository.getSearchResult(keyword))
        }
    }*/
/*

    private val _searchSuggest = MutableStateFlow(SuggestRootEntity())
    val searchSuggest = _searchSuggest
    fun getSearchSuggest(term: String) {
        viewModelScope.launch {
            if (term.isEmpty()) {
                _searchSuggest.emit(SuggestRootEntity())
            } else {
                _searchSuggest.emit(repository.getSuggest(term))
            }
        }
    }
*/

    private val _hotVideo = MutableStateFlow(HotVideoEntity())
    val hotVideo: StateFlow<HotVideoEntity> = _hotVideo
    fun getHotVideo() {
        viewModelScope.launch {
            _hotVideo.emit(repository.getHotVideo())
        }
    }

    /**
     * 改进部分Begin
     */
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

    private val _userCard = MutableStateFlow(UserCardEntity())
    val userCard: StateFlow<UserCardEntity> = _userCard

    fun refreshUserCard(mid: String) {
        viewModelScope.launch {
            _userCard.emit(repository.getUserCard(mid))
        }
    }

    /**
     * 改进部分End
     */

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

    fun setStaggeredGrid(status: Boolean) {
        viewModelScope.launch {
            _isStaggeredGrid.emit(status)
            sp.edit().putBoolean("isStaggeredGrid", status).apply()
        }
    }


    private var _qrcode = MutableStateFlow(GenerateQrcodeEntity())
    val qrcode: StateFlow<GenerateQrcodeEntity> = _qrcode
    fun getQrcodeUrl() {
        viewModelScope.launch {
            _qrcode.emit(repository.getQrcode())
        }
    }

    private val _status = MutableStateFlow(PollQrcode())
    val status: StateFlow<PollQrcode> = _status
    fun getStatus(key: String) {
        viewModelScope.launch {
            _status.emit(repository.getQrcodeStatus(key))
        }
    }

    private val _sheetState = MutableStateFlow(
        SheetState(
            visible = false,
            onShowRequest = ::onShowRequest,
            onDismissRequest = ::onDismissRequest
        )
    )
    val sheetState: StateFlow<SheetState> = _sheetState

    private fun onShowRequest() {
        viewModelScope.launch {
            _sheetState.emit(value = _sheetState.value.copy(visible = true))
        }
    }

    private fun onDismissRequest() {
        viewModelScope.launch {
            _sheetState.emit(value = _sheetState.value.copy(visible = false))
        }
    }
}