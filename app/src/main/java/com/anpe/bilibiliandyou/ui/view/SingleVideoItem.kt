package com.anpe.bilibiliandyou.ui.view

import android.content.Intent
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.anpe.bilibiliandyou.R
import com.anpe.bilibiliandyou.entity.related.Data
import com.anpe.bilibiliandyou.ui.activity.details.DetailsActivity
import com.anpe.bilibiliandyou.ui.activity.details.DetailsViewModel
import com.anpe.bilibiliandyou.utils.Utils.Companion.clickableNoRipple
import com.anpe.bilibiliandyou.utils.Utils.Companion.toSimplify

/**
 * 视频推荐模版
 * @param modifier Modifier
 * @param data 数据
 */
@Composable
fun SingleVideoItem(
    modifier: Modifier = Modifier,
    aid: Int,
    image: String,
    title: String,
    name: String,
    view: Int,
    danmaku: Int
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(0.dp, 5.dp, 0.dp, 5.dp)
            .clickableNoRipple {
                Intent(context, DetailsActivity::class.java).apply {
                    this.putExtra("aid", aid)
                    context.startActivity(this)
                }
            }
    ) {
        ConstraintLayout {
            val (imageRef, titleRef, nameRef, viewRef, danmakuRef) = createRefs()

            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio((4f / 3f))
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                model = image,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 7.dp, top = 7.dp, end = 7.dp)
                    .constrainAs(titleRef) {
                        start.linkTo(imageRef.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(nameRef.top)
                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                    },
                text = title,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            MyLabel(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 3.dp)
                    .constrainAs(nameRef) {
                        start.linkTo(imageRef.end)
                        bottom.linkTo(viewRef.top)
                    },
                text = name,
                fontSize = 10.sp,
                icon = R.drawable.baseline_group_24,
                iconSize = 14.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
            )

            MyLabel(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 5.dp)
                    .constrainAs(viewRef) {
                        start.linkTo(imageRef.end,)
                        bottom.linkTo(parent.bottom)
                    },
                text = view.toSimplify(),
                fontSize = 10.sp,
                icon = R.drawable.baseline_play_circle_outline_24,
                iconSize = 15.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
            )

            MyLabel(
                modifier = Modifier
                    .padding(start = 3.dp, bottom = 5.dp)
                    .constrainAs(danmakuRef) {
                        start.linkTo(viewRef.end)
                        bottom.linkTo(parent.bottom)
                    },
                text = danmaku.toSimplify(),
                fontSize = 10.sp,
                icon = R.drawable.baseline_list_alt_24,
                iconSize = 15.dp,
                backgroundColor = MaterialTheme.colorScheme.surface,
            )
        }
    }
}