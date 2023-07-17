package com.anpe.bilibiliandyou.ui.pagers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anpe.bilibiliandyou.ui.activity.main.MainViewModel

@Composable
fun MinePager(
    navControllerScreen: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val sta by viewModel.isStaggeredGrid.collectAsState()

    Column(Modifier.fillMaxSize()) {
        var checked by remember {
            mutableStateOf(sta)
        }
        SettingsSwitchItem(
            title = "首页瀑布流排版",
            tip = "首页内容是否开启瀑布流排版",
            checked = checked,
            onCheckedChange = {
                checked = it
                viewModel.setStaggeredGrid(checked)
            }
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    modifier: Modifier = Modifier,
    title: String,
    tip: String? = null,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)
) {
    Box(
        modifier = modifier
            .clickable {
                onCheckedChange(!checked)
            }
            .fillMaxWidth()
            .padding(15.dp)
            .height(50.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            tip?.apply {
                Text(text = this, fontSize = 14.sp)
            }
        }
        Switch(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            checked = checked,
            onCheckedChange = {
            onCheckedChange(it)
        }
        )
    }
}