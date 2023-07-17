package com.anpe.bilibiliandyou.ui.pagers.manager

import androidx.annotation.StringRes
import com.anpe.bilibiliandyou.R

sealed class PagerManager(val route: String, @StringRes val resourceId: Int) {
    object HomePager: PagerManager("HomePager", R.string.home_pager)
    object MessagePager: PagerManager("MessagePager", R.string.message_pager)
    object MinePager: PagerManager("MinePager", R.string.mine_pager)
}