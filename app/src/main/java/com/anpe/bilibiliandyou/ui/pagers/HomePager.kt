@file:OptIn(ExperimentalFoundationApi::class)

package com.anpe.bilibiliandyou.ui.pagers

import android.content.Intent
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.anpe.bilibiliandyou.R
import com.anpe.bilibiliandyou.network.data.intent.MainIntent
import com.anpe.bilibiliandyou.network.data.model.index.IndexEntityNew
import com.anpe.bilibiliandyou.network.data.model.index.Item
import com.anpe.bilibiliandyou.network.data.state.IndexState
import com.anpe.bilibiliandyou.ui.activity.details.DetailsActivity
import com.anpe.bilibiliandyou.ui.activity.main.MainViewModel
import com.anpe.bilibiliandyou.ui.view.MyLabel
import com.anpe.bilibiliandyou.utils.Utils.Companion.clickableNoRipple
import com.anpe.bilibiliandyou.utils.Utils.Companion.secondToDateString
import com.anpe.bilibiliandyou.utils.Utils.Companion.toSimplify
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anpe.bilibiliandyou.ui.screens.manager.ScreenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePager(navControllerScreen: NavController, viewModel: MainViewModel = viewModel()) {
    val scope = rememberCoroutineScope()

    var list by remember {
        mutableStateOf(listOf<Item>())
    }

    val isStaggeredGrid by viewModel.isStaggeredGrid.collectAsState()

    LaunchedEffect(key1 = true, block = {
        viewModel.indexState.collect {
            when (it) {
                is IndexState.Error -> {}
                is IndexState.Failure -> {}
                IndexState.Idle -> {}
                IndexState.Loading -> {}
                is IndexState.Success -> list = it.indexEntityNew.data.item
            }
        }
    })

    var refreshing by remember {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        scope.launch {
            refreshing = true
            delay(500)
            viewModel.mainIntentChannel.send(MainIntent.GetIndex)
            refreshing = false
        }
    })

    if (list.isNotEmpty()) {
        Box {
            Column(modifier = Modifier.pullRefresh(pullRefreshState)) {
                VideoList(
                    list = list,
                    isStaggeredGrid = isStaggeredGrid,
                    onItemClick = {
//                        navControllerScreen.navigate("${ScreenManager.DetailsScreen.route}/$it")
                        Log.d("Logi", "HomePager: $it")
                    }
                )
            }
            PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun VideoList(
    modifier: Modifier = Modifier,
    list: List<Item>,
    onItemClick: (Int) -> Unit,
    isStaggeredGrid: Boolean = false
) {
    val configuration = LocalConfiguration.current

    val isPad = configuration.screenWidthDp > 1000

    if (isStaggeredGrid && !isPad) {
        LazyVerticalStaggeredGrid(
            modifier = modifier,
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp, 5.dp, 10.dp, 5.dp),
            content = {
                items(list) {
                    VideoItem(
                        modifier = Modifier
                            .clickableNoRipple {
                                /*Intent(context, DetailsActivity::class.java).apply {
                                    this.putExtra("aid", it.id)
                                    context.startActivity(this)
                                }*/
                                onItemClick(it.id)
                            },
                        data = it,
                        minLines = 1
                    )
                }
            }
        )
    } else {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(if (isPad) 4 else 2),
            contentPadding = PaddingValues(10.dp, 5.dp, 10.dp, 5.dp),
            content = {
                items(list) {
                    VideoItem(
                        modifier = Modifier
                            .animateItemPlacement()
                            .clickableNoRipple {
                                /*Intent(context, DetailsActivity::class.java).apply {
                                    this.putExtra("aid", it.id)
                                    context.startActivity(this)
                                }*/
                                onItemClick(it.id)
                            },
                        data = it,
                        minLines = 2
                    )
                }
            }
        )
    }
}

@Composable
private fun VideoItem(modifier: Modifier = Modifier, data: Item, minLines: Int) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current

    val isPad = configuration.screenWidthDp > 1000

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (picRef, viewRef, danmakuRef, durationRef, faceRef, titleRef, nameRef) = createRefs()

            /*Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (isPad) (20f / 11f) else (16f / 10f))
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = ImageRequest.Builder(context)
                        .data(data.pic)
                        .crossfade(true)
                        .size(200)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(if (isPad) 10.dp else 5.dp)
                ) {
                    if (isPad) {
                        MyLabel(
                            modifier = Modifier.alpha(0.8f),
                            text = data.stat.view.toSimplify(),
                            fontSize = 10.sp
                        )
                        MyLabel(
                            modifier = Modifier.alpha(0.8f),
                            text = data.stat.danmaku.toSimplify(),
                            fontSize = 10.sp
                        )
                    }

                    MyLabel(
                        modifier = Modifier.alpha(0.8f),
                        text = data.duration.secondToDateString("m:ss"),
                        fontSize = if (isPad) 10.sp else 9.sp,
                        contentMargin = if (isPad) PaddingValues(
                            5.dp,
                            3.dp,
                            5.dp,
                            3.dp
                        ) else PaddingValues(3.dp, 1.dp, 3.dp, 1.dp)
                    )
                }
            }*/

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (isPad) (20f / 11f) else (16f / 10f))
                    .constrainAs(picRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                model = ImageRequest.Builder(context)
                    .data(data.pic)
                    .crossfade(true)
                    .size(200)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            if (isPad) {
                MyLabel(
                    modifier = Modifier
                        .alpha(0.8f)
                        .constrainAs(viewRef) {
                            start.linkTo(picRef.start, 10.dp)
                            bottom.linkTo(picRef.bottom, 10.dp)
                        },
                    text = data.stat.view.toSimplify(),
                    fontSize = 10.sp
                )
                MyLabel(
                    modifier = Modifier
                        .alpha(0.8f)
                        .constrainAs(danmakuRef) {
                            start.linkTo(viewRef.end, 3.dp)
                            bottom.linkTo(viewRef.bottom)
                        },
                    text = data.stat.danmaku.toSimplify(),
                    fontSize = 10.sp
                )
            }

            MyLabel(
                modifier = Modifier
                    .alpha(0.8f)
                    .padding(if (isPad) 0.dp else 5.dp)
                    .constrainAs(durationRef) {
                        if (!isPad) {
                            start.linkTo(picRef.start)
                            bottom.linkTo(picRef.bottom)
                        } else {
                            start.linkTo(danmakuRef.end, 3.dp)
                            bottom.linkTo(danmakuRef.bottom)
                        }
                    },
                text = data.duration.secondToDateString("m:ss"),
                fontSize = if (isPad) 10.sp else 9.sp,
                contentMargin = if (isPad) PaddingValues(
                    5.dp,
                    3.dp,
                    5.dp,
                    3.dp
                ) else PaddingValues(3.dp, 1.dp, 3.dp, 1.dp)
            )

            AsyncImage(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .constrainAs(faceRef) {
                        bottom.linkTo(picRef.bottom, margin = (-17).dp)
                        end.linkTo(parent.end)
                    },
                model = ImageRequest.Builder(context)
                    .data(data.owner.face)
                    .crossfade(true)
                    .size(100)
                    .build(),
                contentDescription = null
            )

            val margin = if (isPad) 10.dp else 5.dp

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = margin, end = 50.dp, top = margin)
                    .constrainAs(nameRef) {
                        start.linkTo(parent.start)
                        top.linkTo(picRef.bottom)
                    },
                text = data.owner.name,
                maxLines = 1,
                fontSize = if (isPad) 15.sp else 12.sp,
                color = MaterialTheme.colorScheme.secondary,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = margin, end = margin, top = 5.dp, bottom = 10.dp)
                    .constrainAs(titleRef) {
                        start.linkTo(parent.start)
                        top.linkTo(nameRef.bottom)
                        end.linkTo(parent.end)
                    },
                text = data.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                minLines = minLines,
                fontSize = if (isPad) 16.sp else 13.sp,
                lineHeight = if (isPad) 20.sp else 18.sp,
                overflow = TextOverflow.Ellipsis
            )

            if (!isPad) {
                MyLabel(
                    modifier = Modifier
                        .padding(start = 5.dp, bottom = 5.dp)
                        .constrainAs(viewRef) {
                            start.linkTo(parent.start)
                            top.linkTo(titleRef.bottom)
                        },
                    text = data.stat.view.toSimplify(),
                    fontSize = 10.sp,
                    icon = R.drawable.baseline_play_circle_outline_24,
                    iconSize = 15.dp,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    radio = 8.dp
                )

                MyLabel(
                    modifier = Modifier
                        .padding(start = 3.dp, bottom = 5.dp)
                        .constrainAs(danmakuRef) {
                            start.linkTo(viewRef.end)
                            top.linkTo(viewRef.top)
                        },
                    text = data.stat.danmaku.toSimplify(),
                    fontSize = 10.sp,
                    icon = R.drawable.baseline_list_alt_24,
                    iconSize = 15.dp,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    radio = 8.dp
                )
            }
        }
    }
}