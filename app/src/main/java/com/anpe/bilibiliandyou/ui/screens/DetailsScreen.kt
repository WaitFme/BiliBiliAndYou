@file:UnstableApi @file:OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

package com.anpe.bilibiliandyou.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.anpe.bilibiliandyou.R
import com.anpe.bilibiliandyou.entity.related.Data
import com.anpe.bilibiliandyou.ui.activity.main.MainViewModel
import com.anpe.bilibiliandyou.ui.view.MyLabel
import com.anpe.bilibiliandyou.ui.view.TextIcon
import com.anpe.bilibiliandyou.utils.Utils.Companion.clickableNoRipple
import com.anpe.bilibiliandyou.utils.Utils.Companion.secondToDateString
import com.anpe.bilibiliandyou.utils.Utils.Companion.toSimplify

@Composable
fun DetailsScreen(
    navControllerScreen: NavController,
    aid: Int? = 2,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    val videoInfo by viewModel.view.collectAsState()

    aid?.apply {
        viewModel.getView(this.toLong())
        viewModel.getRelatedInfo(this.toLong())

        videoInfo.data?.let {
            viewModel.refreshUserCard(it.owner.mid)
            viewModel.getPlayUrl(this.toLong(), it.cid.toLong())
        }
    }

    val mExoPlayer = ExoPlayer.Builder(context).build()

    clipboardManager = context.getSystemService(
        ComponentActivity.CLIPBOARD_SERVICE
    ) as ClipboardManager

    DetailsContent(mExoPlayer, viewModel)
}

private lateinit var clipboardManager: ClipboardManager

@Composable
private fun DetailsContent(exoPlayer: ExoPlayer, viewModel: MainViewModel) {
    val view by viewModel.view.collectAsState()
    val userCard by viewModel.userCard.collectAsState()

    view.data?.let { viewData ->
        userCard.data?.let { userData ->
            Column {
                val configuration = LocalConfiguration.current

                val pagerState = rememberPagerState()

                val isPad by remember {
                    mutableStateOf(configuration.screenWidthDp.dp > 1000.dp)
                }

                if (!isPad) {
                    Column(
                        modifier = Modifier
                            .statusBarsPadding()
                            .fillMaxWidth()
                    ) {
                        VideoUpInfo(
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
                                .fillMaxWidth(),
                        )

                        VideoBlock(
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, bottom = 10.dp),
                            exoPlayer = exoPlayer,
                            viewModel = viewModel
                        )

                        HorizontalPager(pageCount = 2, state = pagerState) {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(start = 15.dp, end = 15.dp)
                            ) {
                                var isSynopsis by remember {
                                    mutableStateOf(false)
                                }

                                VideoInfoTitle(
                                    Modifier
                                        .padding(bottom = 10.dp)
                                        .clickable { isSynopsis = !isSynopsis },
                                    title = viewData.title,
                                    titleFontSize = 17.sp
                                )

                                VideoInfoLabels(modifier = Modifier.padding(bottom = 10.dp))

                                AnimatedVisibility(visible = isSynopsis) {
                                    VideoInfoDesc(desc = viewData.desc, fontSize = 13.sp)
                                }

                                VideoConsole(Modifier.padding(bottom = 10.dp))

                                RelatedBlock(viewModel = viewModel)
                            }
                        }
                    }
                } else {
                    Row {
                        Column(
                            Modifier
                                .fillMaxWidth(0.7f)
                                .statusBarsPadding()
                        ) {
                            var isSynopsis by remember {
                                mutableStateOf(true)
                            }

                            VideoInfoTitle(
                                Modifier
                                    .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                                    .clickable { isSynopsis = !isSynopsis },
                                title = viewData.title,
                                titleFontSize = 25.sp
                            )

                            VideoInfoLabels(
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    bottom = 10.dp
                                )
                            )

                            VideoBlock(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                                exoPlayer = exoPlayer,
                                viewModel = viewModel
                            )

                            AnimatedVisibility(visible = isSynopsis) {
                                VideoInfoDesc(
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                                    desc = viewData.desc,
                                    fontSize = 15.sp
                                )
                            }
                        }

                        Column {
                            VideoUpInfo(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                                    .statusBarsPadding()
                                    .fillMaxWidth(),
                            )

                            HorizontalPager(pageCount = 2) {
                                RelatedBlock(
                                    modifier = Modifier.padding(
                                        start = 10.dp,
                                        end = 10.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * title 视频作者信息
 */
@Composable
private fun VideoUpInfo(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val view by viewModel.view.collectAsState()
    val user by viewModel.userCard.collectAsState()

    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (image, button, info) = createRefs()

        AsyncImage(
            modifier = Modifier
                .size(35.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    centerVerticallyTo(parent)
                }
                .clip(CircleShape),
            model = view.data?.owner?.face,
            contentDescription = null
        )

        Column(
            Modifier
                .padding(start = 10.dp)
                .constrainAs(info) {
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                    centerVerticallyTo(parent)
                    start.linkTo(image.end)
                }
        ) {
            Text(text = view.data?.owner?.name.toString(), fontSize = 14.sp)
            Text(text = "${user.data?.follower?.toSimplify()}粉丝", fontSize = 11.sp)
        }

        Button(
            modifier = Modifier
                .constrainAs(button) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)
                },
            onClick = {

            }
        ) {
            Text(text = if (user.data?.following!!) "已关注" else "未关注")
        }
    }
}

/**
 * title 视频标题
 */
@Composable
private fun VideoInfoTitle(
    modifier: Modifier = Modifier,
    title: String,
    titleFontSize: TextUnit
) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = titleFontSize
    )
}

/**
 * title 视频信息标签
 */
@Composable
private fun VideoInfoLabels(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val view by viewModel.view.collectAsState()

    view.data?.let {
        val labels = remember {
            mutableStateListOf(
                "${it.stat.view.toSimplify()}播放",
                "${it.stat.danmaku.toSimplify()}弹幕",
                it.pubdate.secondToDateString("yyyy/MM/dd HH:mm"),
                if (it.copyright == 1) {
                    "原创"
                } else {
                    "转载"
                },
                "AV${it.stat.aid}",
            )
        }

        FlowRow(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            labels.forEach {
                MyLabel(
                    modifier = Modifier
                        .padding(2.dp)
                        .clickableNoRipple {
                            // Creates a new text clip to put on the clipboard
                            val clip: ClipData = ClipData.newPlainText("video aid", it)
                            clipboardManager.setPrimaryClip(clip)
                        },
                    text = it
                )
            }
        }
    }
}

/**
 * title 简介模块
 */
@Composable
private fun VideoInfoDesc(modifier: Modifier = Modifier, desc: String, fontSize: TextUnit) {
    SelectionContainer(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = desc, fontSize = fontSize)
    }
}

/**
 * title 视频模块
 */
@Composable
private fun VideoBlock(
    modifier: Modifier = Modifier,
    exoPlayer: ExoPlayer,
    viewModel: MainViewModel = viewModel()
) {
    val playUrl by viewModel.playUrl.collectAsState()

    playUrl.data?.apply {
        val map = mapOf("Referer" to "https://www.bilibili.com")

        val mediaItem = MediaItem.fromUri(durl[0].url)

        val factory = DefaultHttpDataSource.Factory().apply {
            setDefaultRequestProperties(map)
            setUserAgent("https://www.bilibili.com")
        }

        val mediaSource = DefaultMediaSourceFactory(factory).createMediaSource(mediaItem)

        exoPlayer.apply {
            playWhenReady = true
            setMediaSource(mediaSource)
            prepare()
        }

        Box(modifier = modifier) {
            Spacer(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .aspectRatio((16f / 9f))
                    .fillMaxWidth()
                    .background(Color.Black)
            )
            AndroidView(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .aspectRatio((16f / 9f))
                    .fillMaxWidth(),
                factory = { context ->
                    PlayerView(context).apply {
                        this.player = exoPlayer
                    }
                }
            )
        }
    }
}

/**
 * title 视频操作模块
 */
@Composable
private fun VideoConsole(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val view by viewModel.view.collectAsState()

    view.data?.apply {
        val icons = remember {
            mutableStateListOf(
                R.drawable.ic_like_24,
                R.drawable.ic_unlike_24,
                R.drawable.ic_coin_24,
                R.drawable.ic_collection_24,
                R.drawable.ic_sharelive_24
            )
        }

        val texts = remember {
            mutableStateListOf(
                stat.like.toSimplify(),
                "踩",
                stat.coin.toSimplify(),
                stat.favorite.toSimplify(),
                "分享",
            )
        }

        val tint by animateColorAsState(
            targetValue = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
            label = "tint"
        )

        Row(modifier = modifier) {
            for (index in 0 until icons.size) {
                IconButton(modifier = Modifier.weight(1f), onClick = {

                }) {
                    TextIcon(
                        iconId = icons[index],
                        iconTint = tint,
                        text = texts[index]
                    )
                }
            }
        }
    }
}

/**
 * title 视频推荐模块
 */
@Composable
private fun RelatedBlock(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MainViewModel = viewModel()
) {
    val related by viewModel.related.collectAsState()

    related.data?.let {
        LazyColumn(modifier = modifier, contentPadding = contentPadding) {
            it.forEach {
                item {
                    RelatedItem(data = it)
                }
            }
        }
    }
}

/**
 * title 视频推荐模版
 */
@Composable
private fun RelatedItem(modifier: Modifier = Modifier, data: Data) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(0.dp, 5.dp, 0.dp, 5.dp)
            .clickableNoRipple {
                /*Intent(context, DetailsActivity::class.java).apply {
                    this.putExtra("aid", data.aid)
                    context.startActivity(this)
                }*/
            }
    ) {
        ConstraintLayout {
            val (image, title, name, view, danmaku) = createRefs()

            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio((4f / 3f))
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                model = data.pic,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 7.dp, top = 7.dp, end = 7.dp)
                    .constrainAs(title) {
                        start.linkTo(image.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(name.top)
                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                    },
                text = data.title,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            MyLabel(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 3.dp)
                    .constrainAs(name) {
                        start.linkTo(image.end)
                        bottom.linkTo(view.top)
                    },
                text = data.owner.name,
                fontSize = 10.sp,
                icon = R.drawable.baseline_group_24,
                iconSize = 14.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
            )

            MyLabel(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 5.dp)
                    .constrainAs(view) {
                        start.linkTo(image.end,)
                        bottom.linkTo(parent.bottom)
                    },
                text = data.stat.view.toSimplify(),
                fontSize = 10.sp,
                icon = R.drawable.baseline_play_circle_outline_24,
                iconSize = 15.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
            )

            MyLabel(
                modifier = Modifier
                    .padding(start = 3.dp, bottom = 5.dp)
                    .constrainAs(danmaku) {
                        start.linkTo(view.end)
                        bottom.linkTo(parent.bottom)
                    },
                text = data.stat.danmaku.toSimplify(),
                fontSize = 10.sp,
                icon = R.drawable.baseline_list_alt_24,
                iconSize = 15.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
            )
        }
    }
}