@file:OptIn(ExperimentalMaterial3Api::class)

package com.anpe.bilibiliandyou.ui.screens

import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.anpe.bilibiliandyou.R
import com.anpe.bilibiliandyou.network.data.intent.MainIntent
import com.anpe.bilibiliandyou.network.data.state.SuggestState
import com.anpe.bilibiliandyou.ui.activity.details.DetailsActivity
import com.anpe.bilibiliandyou.ui.activity.main.MainViewModel
import com.anpe.bilibiliandyou.ui.pagers.HomePager
import com.anpe.bilibiliandyou.ui.pagers.MessagePager
import com.anpe.bilibiliandyou.ui.pagers.MinePager
import com.anpe.bilibiliandyou.ui.pagers.manager.PagerManager
import com.anpe.bilibiliandyou.ui.screens.manager.ScreenManager
import com.anpe.bilibiliandyou.ui.view.MyScaffold
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navControllerScreen: NavController, viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()

    val configuration = LocalConfiguration.current

    val viewState by viewModel.sheetState.collectAsState()

    MyScaffold(
        topBar = { TopBar(navControllerScreen = navControllerScreen) },
        bottomBar = { BottomBar(navController = navController) },
        railBar = { RailBar(navController = navController) },
        content = {
            Content(
                navControllerScreen = navControllerScreen,
                navController = navController,
                viewModel = viewModel
            )
        },
        sheetContent = { SheetContent(onDismissRequest = viewState.onDismissRequest) },
        configuration = configuration,
        changeValue = 1000.dp,
        visible = viewState.visible,
        cancelable = true,
        canceledOnTouchOutside = true,
        onDismissRequest = viewState.onDismissRequest,
    )
}

@Composable
private fun SheetContent(onDismissRequest: () -> Unit, viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
//    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .width(400.dp)
            .fillMaxHeight(0.5f)
            .clip(shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackHandler {
            onDismissRequest()
        }
    }
}

@Composable
private fun TopBar(navControllerScreen: NavController) {
    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()

    val vm: MainViewModel = viewModel()

//    val data by vm.searchSuggest.collectAsState()

    val suggest = {
        scope.launch {
            vm.suggestState.collect {
                when (it) {
                    is SuggestState.Idle -> {
                        Log.d("MVITEST", "TopBar: IDLE")
                    }

                    is SuggestState.Loading -> {
                        Log.d("MVITEST", "TopBar: Loading")
                    }

                    is SuggestState.Success -> {
                        Log.d("MVITEST", "TopBar: ${it.suggestEntity}")
                    }

                    is SuggestState.Failure -> {

                    }

                    is SuggestState.Error -> {
                        Log.d("MVITEST", "TopBar: Error")
                    }
                }
            }

        }
    }

    val isPad = configuration.screenWidthDp > 1000

    var searchState by remember {
        mutableStateOf(isPad)
    }
    var status by remember {
        mutableStateOf(false)
    }

    var dialog by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        modifier = Modifier,
        title = {
            val alpha by animateFloatAsState(
                targetValue = if (!searchState || !isPad) 1.0f else 0f,
                label = ""
            )
            AnimatedVisibility(visible = !searchState || !isPad,
                enter = slideIn {
                    IntOffset(-it.width * 2, 0)
                },
                exit = slideOut {
                    IntOffset(-(it.width * 2), 0)
                }
            ) {
                Text(modifier = Modifier.alpha(alpha), text = stringResource(R.string.app_name))
            }
        },
        actions = {
            var text by remember {
                mutableStateOf("")
            }
            var texts by remember {
                mutableStateOf(TextFieldValue)
            }

            if (searchState) {
                BackHandler {
                    searchState = false
                }
            }

            if (!searchState) {
                IconButton(onClick = {
                    searchState = true
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }
            }

            AnimatedVisibility(visible = searchState) {
                Column {
                    TextField(
                        modifier = Modifier
                            .width(450.dp)
                            .padding(start = 15.dp, end = 15.dp)
//                            .fillMaxWidth()
                            .background(Color.White),
                        value = text,
                        label = {
                            Text(text = "种植园荣耀")
                        },
                        maxLines = 1,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search"
                            )
                        },
                        onValueChange = {
                            text = it
//                            vm.getSearchSuggest(tv)
                            scope.launch {
                                vm.mainIntentChannel.send(MainIntent.GetSearchSuggest(text))
                            }

                            status = true
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (text.isEmpty()) {
                                val test = "种植园荣耀"
                                navControllerScreen.navigate("${ScreenManager.SearchScreen.route}/$test")
                            } else {
                                navControllerScreen.navigate("${ScreenManager.SearchScreen.route}/$text")
                            }
//                            navControllerScreen.navigate("${ScreenManager.SearchScreen}")
                        })
                    )
                    AnimatedVisibility(visible = status) {
                        Popup(
                            onDismissRequest = {
                                status = false
                            }
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(450.dp)
                                    .padding(start = 15.dp, end = 15.dp),
                                shape =
                                RoundedCornerShape(
                                    bottomStart = 15.dp,
                                    bottomEnd = 15.dp
                                )
                            ) {
                                /*LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    data.data?.forEach {
                                        item {
                                            Text(
                                                text = it.value, modifier = Modifier
                                                    .padding(start = 15.dp, end = 15.dp)
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        vm.getSearchResult(it.value)
                                                    }
                                            )
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                }
            }

            IconButton(onClick = {
                dialog = !dialog
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_supervised_user_circle_24),
                    contentDescription = null
                )
            }
        }
    )

    if (dialog) {
        if (!isPad) {
            BackHandler {
                dialog = false
            }
        }
    }

    if (dialog) {
        CustomDialog(onDismissRequest = { dialog = false }, navController = navControllerScreen)
    }
}

@Composable
private fun TopBarNew(navControllerScreen: NavController) {
    val context = LocalContext.current

    val vm: MainViewModel = viewModel()

    var searchState by remember {
        mutableStateOf(false)
    }

    var searchValue by remember {
        mutableStateOf(TextFieldValue())
    }

    var dialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(15.dp, 10.dp, 15.dp, 10.dp)
    ) {
//        val containerColor = FilledTextFieldTokens.ContainerColor.toColor()
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp)),
            value = searchValue,
            placeholder = { Text(text = stringResource(id = R.string.app_name)) },
            leadingIcon = {
                Icon(
                    modifier = Modifier.padding(start = 10.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier.padding(end = 3.dp),
                    onClick = {
                        /*Intent(context, DetailsActivity::class.java).apply {
                            this.putExtra("aid", 827088226)
                            context.startActivity(this)
                        }*/
                        dialog = !dialog
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_supervised_user_circle_24),
                        contentDescription = null
                    )
                }
            },
            onValueChange = {
                searchValue = it
            },
            maxLines = 1,
            keyboardActions = KeyboardActions(onSearch = {
                try {
                    val aid =
                        if (searchValue.text.isEmpty()) 827088226 else searchValue.text.toInt()
                    Intent(context, DetailsActivity::class.java).apply {
                        putExtra("aid", aid)
                        context.startActivity(this)
                    }
                } catch (e: NumberFormatException) {
                    Log.e("ERROR", "TopBarNew: $ e")
                }
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            colors = TextFieldDefaults.colors(
//                focusedContainerColor = containerColor,
//                unfocusedContainerColor = containerColor,
//                disabledContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,      // 有焦点时 底部指示条为透明
                unfocusedIndicatorColor = Color.Transparent,    // 无焦点，为绿色
                disabledIndicatorColor = Color.Gray,            // 不可用，灰色
                errorIndicatorColor = Color.Red,                // 错误时，为红色
            )
        )

        /*var index by remember {
            mutableStateOf(1)
        }

        TabRow(selectedTabIndex = index) {
            Tab(
                selected = false,
                onClick = { index = 0 },
                text = { Text(text = "推荐") }
            )
            Tab(selected = true, onClick = { index = 1 }, text = { Text(text = "热门") })
            Tab(selected = false, onClick = { index = 2 }, text = { Text(text = "排行") })
        }*/
    }

    if (dialog) {
        BackHandler {
            dialog = false
        }
    }

    if (dialog) {
        /*Dialog(onDismissRequest = { dialog = false }) {
            Column(
                modifier = Modifier
                    .width(400.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                modifier = Modifier.size(50.dp),
                                model = R.drawable.baseline_supervised_user_circle_24,
                                contentDescription = null
                            )
                            Column {
                                Text(text = "游客")
                                Text(text = "LV-1")
                            }
                        }

                        TextButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
                            navControllerScreen.navigate("LoginScreen")
                        }) {
                            Text(text = "登陆")
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "0\n关注",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "0\n粉丝",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "0\n动态",
                            textAlign = TextAlign.Center
                        )
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), textAlign = TextAlign.Center, text = "历史记录"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), textAlign = TextAlign.Start, text = "稍后再看"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), textAlign = TextAlign.Start, text = "设置"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), textAlign = TextAlign.Start, text = "关于"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), textAlign = TextAlign.Start, text = "登陆"
                    )
                }
            }
        }*/

        CustomDialog(onDismissRequest = { dialog = false }, navController = navControllerScreen)
    }

    /*Column {
        TopAppBar(
            modifier = Modifier,
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                AnimatedVisibility(visible = searchState) {
                    IconButton(onClick = {
                        searchState = false
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            },
            title = {
                Text(text = stringResource(R.string.app_name))
            },
            actions = {
                val tv = remember {
                    mutableStateOf("")
                }

                if (searchState) {
                    BackHandler {
                        searchState = false
                    }
                }

                if (!searchState) {
                    IconButton(onClick = {
                        searchState = true
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                }

                AnimatedVisibility(visible = searchState) {
                    TextField(
                        modifier = Modifier
                            .width(110.dp)
                            .background(Color.White),
                        value = tv.value,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search"
                            )
                        },
//                        placeholder = { Text(text = stringResource(id = R.string.search_tip)) },
                        onValueChange = {
                            tv.value = it
//                            vm.allWatchFlow = vm.findWatchFlow(1)
                        }
                    )
                }

                IconButton(onClick = {}) {
                    Icon(painter = painterResource(id = R.drawable.baseline_supervised_user_circle_24), contentDescription = null)
                }
            }
        )
    }*/
}

@Composable
private fun CustomDialog(onDismissRequest: () -> Unit, navController: NavController) {
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Column(
            modifier = Modifier
                .width(400.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                text = stringResource(id = R.string.app_name),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            modifier = Modifier
                                .size(70.dp)
                                .padding(15.dp)
                                .clickable {
                                    navController.navigate("LoginScreen")
                                },
                            model = R.drawable.baseline_supervised_user_circle_24,
                            contentDescription = null
                        )
                        Column {
                            Text(text = "游客", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "LV-1", fontSize = 11.sp)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "0\n关注",
                        textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "0\n粉丝",
                        textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "0\n动态",
                        textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                IconText(text = "我的消息", icon = R.drawable.baseline_message_24)
                IconText(text = "我的收藏", icon = R.drawable.ic_collection_24)
                IconText(text = "稍后再看", icon = R.drawable.ic_sharelive_24)
                IconText(text = "历史记录", icon = R.drawable.baseline_history_24)
                IconText(text = "退出登陆", icon = R.drawable.baseline_block_24)
            }

            IconText(
                modifier = Modifier
                    .padding(start = 10.dp),
                text = "设置", icon = R.drawable.baseline_settings_24
            )
            IconText(
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp),
                text = "关于", icon = R.drawable.baseline_error_24
            )
        }
    }
}

@Composable
private fun IconText(modifier: Modifier = Modifier, text: String, icon: Int? = null) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                modifier = Modifier
                    .padding(start = 30.dp)
                    .size(20.dp),
                painter = painterResource(id = icon),
                contentDescription = "icon"
            )
        }

        Text(
            modifier = Modifier
                .padding(start = if (icon == null) 30.dp else 15.dp),
            text = text, fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Content(
    navControllerScreen: NavController,
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = PagerManager.HomePager.route,
        builder = {
            composable(PagerManager.HomePager.route) { HomePager(navControllerScreen, viewModel) }
            composable(PagerManager.MessagePager.route) { MessagePager(navControllerScreen, viewModel) }
            composable(PagerManager.MinePager.route) { MinePager(navControllerScreen, viewModel) }
        }
    )
}

@Composable
private fun RailBar(navController: NavHostController) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        content = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val items = listOf(
                PagerManager.HomePager,
                PagerManager.MessagePager,
                PagerManager.MinePager,
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items.forEach { pager ->
                    NavigationRailItem(
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 5.dp),
                        selected = currentDestination?.hierarchy?.any { it.route == pager.route } == true,
                        icon = {
                            Icon(
                                when (pager.route) {
                                    "HomePager" -> painterResource(id = R.drawable.baseline_home_24)
                                    "MessagePager" -> painterResource(id = R.drawable.baseline_message_24)
                                    "MinePager" -> painterResource(id = R.drawable.baseline_live_tv_24)
                                    else -> painterResource(id = R.drawable.baseline_live_tv_24)
                                }, contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                stringResource(pager.resourceId),
                                fontSize = 14.sp
                            )
                        },
                        alwaysShowLabel = true,
                        onClick = {
                            navController.navigate(pager.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun BottomBar(navController: NavHostController) {
    NavigationBar {
        val vm: MainViewModel = viewModel()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val items = listOf(
            PagerManager.HomePager,
            PagerManager.MessagePager,
            PagerManager.MinePager,
        )

        items.forEach { pager ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == pager.route } == true,
                icon = {
                    Icon(
                        when (pager.route) {
                            "HomePager" -> painterResource(id = R.drawable.baseline_home_24)
                            "MessagePager" -> painterResource(id = R.drawable.baseline_message_24)
                            "MinePager" -> painterResource(id = R.drawable.baseline_live_tv_24)
                            else -> painterResource(id = R.drawable.baseline_live_tv_24)
                        }, contentDescription = null
                    )
                },
                label = {
                    Text(
                        stringResource(pager.resourceId),
                        fontSize = 14.sp
                    )
                },
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(pager.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}