package com.anpe.bilibiliandyou.network.data.state

data class SheetState(
    val visible: Boolean,
    val onShowRequest: () -> Unit,
    val onDismissRequest: () -> Unit
)