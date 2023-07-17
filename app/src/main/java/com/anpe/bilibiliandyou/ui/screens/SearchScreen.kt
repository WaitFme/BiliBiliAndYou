@file:OptIn(ExperimentalMaterial3Api::class)

package com.anpe.bilibiliandyou.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anpe.bilibiliandyou.network.data.intent.MainIntent
import com.anpe.bilibiliandyou.network.data.model.search.DataX
import com.anpe.bilibiliandyou.network.data.state.SearchState
import com.anpe.bilibiliandyou.ui.activity.main.MainViewModel
import com.anpe.bilibiliandyou.ui.view.SingleVideoItem
import com.anpe.bilibiliandyou.utils.Utils.Companion.fromHtmlToString
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navControllerScreen: NavController,
    keyword: String? = "种植园荣耀",
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var list by remember {
        mutableStateOf(listOf<DataX>())
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.mainIntentChannel.send(MainIntent.GetSearchResult(keyword ?: "种植园荣耀"))

        viewModel.searchState.collect {
            when (it) {
                is SearchState.Error -> {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }

                SearchState.Idle -> {}
                SearchState.Loading -> {
                    println("loding")
                }

                is SearchState.Success -> {
                    println("succes")
                    it.searchEntity.data.result.forEach { data ->
                        if (data.resultType == "video") {
                            list = data.data
                            println(list)
                        }
                    }
                }
            }
        }
    })

    Scaffold(
        topBar = {
            TopBar(
                navControllerScreen = navControllerScreen,
                keyword = keyword ?: "种植园荣耀"
            ) {
                scope.launch {
                    viewModel.mainIntentChannel.send(MainIntent.GetSearchResult(it))
                }
            }
        }
    ) {
        SearchResultBlock(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp),
            list = list
        )
    }
}

@Composable
private fun TopBar(
    navControllerScreen: NavController,
    keyword: String,
    onSearch: (String) -> Unit
) {
    val context = LocalContext.current

    var searchValue by remember {
        mutableStateOf(TextFieldValue(text = keyword))
    }
    var s by remember {
        mutableStateOf(keyword)
    }

    TopAppBar(title = { /*TODO*/ },
        actions = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                query = s,
                onQueryChange = {
                    s = it
                },
                placeholder = { Text(text = "search something") },
                leadingIcon = {
                    IconButton(onClick = {
                        navControllerScreen.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                onSearch = {
                    onSearch(it)
                },
                active = false,
                onActiveChange = {}
            ) {

            }
        }
    )
/*
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(15.dp, 10.dp, 15.dp, 10.dp)
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp)),
            value = searchValue,
            placeholder = { Text(text = "search something") },
            leadingIcon = {
                IconButton(onClick = {
                    navControllerScreen.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back"
                    )
                }
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            onValueChange = {
                searchValue = it
            },
            maxLines = 1,
            keyboardActions = KeyboardActions(onSearch = {
                if (searchValue.text.isEmpty()) {
                    onSearch(keyword)
                } else {
                    onSearch(searchValue.text)
                }
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,      // 有焦点时 底部指示条为透明
                unfocusedIndicatorColor = Color.Transparent,    // 无焦点，为绿色
                disabledIndicatorColor = Color.Gray,            // 不可用，灰色
                errorIndicatorColor = Color.Red,                // 错误时，为红色
            )
        )
    }*/
}

/**
 * title 视频推荐模块
 */
@Composable
private fun SearchResultBlock(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    list: List<DataX>,
) {
    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
        items(list) {
            SingleVideoItem(
                aid = it.aid,
                image = "https://" + it.pic.substring(2 until it.pic.length),
                title = it.title.fromHtmlToString(),
                name = it.author,
                view = it.play,
                danmaku = it.danmaku
            )
        }
    }
}