package com.anpe.bilibiliandyou.ui.pagers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anpe.bilibiliandyou.network.data.intent.MainIntent
import com.anpe.bilibiliandyou.network.data.model.hotWord.Data
import com.anpe.bilibiliandyou.network.data.state.HotWordState
import com.anpe.bilibiliandyou.ui.activity.main.MainViewModel
import com.anpe.bilibiliandyou.ui.screens.manager.ScreenManager

@Composable
fun MessagePager(navControllerScreen: NavController, viewModel: MainViewModel = viewModel()) {
    var hotWordList by remember {
        mutableStateOf(listOf<Data>())
    }
    var hotBlockStatus by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.mainIntentChannel.send(MainIntent.GetHotWord)

        viewModel.hotWordState.collect {
            when (it) {
                is HotWordState.Success -> {
                    hotWordList = it.hotWordEntity.list
                    hotBlockStatus = true
                }

                else -> {
                    hotBlockStatus = false
                }
            }
        }
    })

    Column {
        HotWordBlock(navControllerScreen, hotWordList)
    }
}

@Composable
private fun HotWordBlock(
    navControllerScreen: NavController,
    list: List<Data>
) {
    if (list.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.padding(start = 15.dp, top = 5.dp, end = 15.dp),
                text = "BiliBili热搜",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            LazyVerticalGrid(
                contentPadding = PaddingValues(10.dp),
                columns = GridCells.Fixed(2),
                content = {
                    list.forEach {
                        item {
                            Text(modifier = Modifier
                                .padding(5.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .clickable {
                                    navControllerScreen.navigate("${ScreenManager.SearchScreen.route}/${it.showName}")
                                },
                                text = it.showName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            )
        }
    }
}
