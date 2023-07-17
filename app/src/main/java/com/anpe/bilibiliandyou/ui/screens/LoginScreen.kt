@file:OptIn(ExperimentalMaterial3Api::class)

package com.anpe.bilibiliandyou.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.anpe.bilibiliandyou.entity.login.GenerateQrcodeEntity
import com.anpe.bilibiliandyou.entity.login.PollQrcode
import com.anpe.bilibiliandyou.ui.activity.main.MainViewModel
import com.anpe.bilibiliandyou.utils.Utils.Companion.generateQrcode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: MainViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()

        viewModel.getQrcodeUrl()

        val qrcodeEntity by viewModel.qrcode.collectAsState()
        val isAccountPager by remember {
            mutableStateOf(true)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "LoginScreen") },
                    actions = {
                        IconButton(onClick = {
                            if (isAccountPager) {
                                navController.navigate("Qrcode")
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
                        }
                    }
                )
            }
        ) {
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                navController = navController,
                startDestination = "Account",
                builder = {
                    composable("Account") { AccountLoginPager() }
                    composable("Qrcode") { QrcodeLoginPager() }
                }
            )
        }
    }
}

@Composable
private fun AccountLoginPager(modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxSize()
    ) {
        val (acc, passwd, login) = createRefs()

        var accText by remember {
            mutableStateOf("")
        }
        var passwdText by remember {
            mutableStateOf("")
        }

        OutlinedTextField(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .constrainAs(acc) {},
            value = accText,
            label = {
                Text(text = "Acc")
            },
            onValueChange = {
                accText = it
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth(0.6f)
                .constrainAs(passwd) {
                    top.linkTo(acc.bottom)
                },
            value = passwdText,
            label = {
                Text(text = "Password")
            },
            onValueChange = {
                passwdText = it
            }
        )

        Button(
            modifier = Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .constrainAs(login) {
                    top.linkTo(passwd.bottom)
                },
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Login")
        }
    }
}

@Composable
private fun QrcodeLoginPager(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    val qrcodeEntity by viewModel.qrcode.collectAsState()
    val status by viewModel.status.collectAsState()
    val scope = rememberCoroutineScope()

    qrcodeEntity.data?.let {
        scope.launch {
            while (true) {
//                getStatus(it.qrcode_key)
                delay(100)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            status.data?.let {
                Text(text = when (it.code) {
                    0 -> "扫码登录成功"
                    86038 -> "二维码已失效"
                    86090 -> "二维码已扫码未确认"
                    86101 -> "未扫码"
                    else -> { "UNKNOWN" }
                })
            }

            Button(onClick = {
//                getQrcodeUrl()
            }) {
                Text(text = "刷新")
            }

            AsyncImage(
                modifier = modifier.fillMaxWidth(),
                model = it.url.generateQrcode(onColor = Color.Black.toArgb()),
                contentDescription = "loginQrcodeImage"
            )
        }
    }
}