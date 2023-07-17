package com.anpe.bilibiliandyou.ui.screens.manager

import androidx.annotation.StringRes
import com.anpe.bilibiliandyou.R

sealed class ScreenManager(val route: String, @StringRes val resourceId: Int) {
    object SplashScreen: ScreenManager("SplashScreen", R.string.splash_screen)
    object MainScreen: ScreenManager("MainScreen", R.string.main_screen)
    object DetailsScreen: ScreenManager("DetailsScreen", R.string.details_screen)
    object LoginScreen: ScreenManager("LoginScreen", R.string.details_screen)
    object SearchScreen: ScreenManager("SearchScreen", R.string.search_screen)
}