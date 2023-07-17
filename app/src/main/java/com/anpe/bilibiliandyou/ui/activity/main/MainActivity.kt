package com.anpe.bilibiliandyou.ui.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anpe.bilibiliandyou.ui.pagers.manager.PagerManager
import com.anpe.bilibiliandyou.ui.screens.DetailsScreen
import com.anpe.bilibiliandyou.ui.screens.LoginScreen
import com.anpe.bilibiliandyou.ui.screens.MainScreen
import com.anpe.bilibiliandyou.ui.screens.SearchScreen
import com.anpe.bilibiliandyou.ui.screens.SplashScreen
import com.anpe.bilibiliandyou.ui.screens.manager.ScreenManager
import com.anpe.bilibiliandyou.ui.theme.BiliBiliAndYouTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            rememberSystemUiController().run {
                setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = !isSystemInDarkTheme()
                )
                setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = !isSystemInDarkTheme(),
                    navigationBarContrastEnforced = false
                )
            }

            BiliBiliAndYouTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = ScreenManager.SplashScreen.route,
                        builder = {
                            composable(ScreenManager.SplashScreen.route) {
                                SplashScreen(
                                    navController
                                )
                            }
                            composable(ScreenManager.MainScreen.route) { MainScreen(navController) }
                            composable(ScreenManager.LoginScreen.route) { LoginScreen() }
                            composable(
                                route = "${ScreenManager.DetailsScreen.route}/{aid}",
                                arguments = listOf(navArgument("aid") { type = NavType.IntType })
                            ) {
                                DetailsScreen(
                                    navControllerScreen = navController,
                                    aid = it.arguments?.getInt("aid"),
                                )
                            }
                            composable(
                                route = "${ScreenManager.SearchScreen.route}/{keyword}",
                                arguments = listOf(navArgument("keyword") {
                                    type = NavType.StringType
                                })
                            ) {
                                SearchScreen(
                                    navControllerScreen = navController,
                                    keyword = it.arguments?.getString("keyword")
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}