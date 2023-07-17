package com.anpe.bilibiliandyou.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LoginService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun initial() {
        /*CoroutineScope(Dispatchers.Default).launch {
            val qrcodeEntity = loginRepository.getQrcode()

            if (qrcodeEntity.code == 0) {
                val url = qrcodeEntity.data?.url
                val qrcodeKey = qrcodeEntity.data?.qrcode_key

                while (true) {
//                    val status = loginRepository.getQrcodeStatus(qrcodeKey)
                    val token = status.data.refresh_token

                    when(status.data.code) {
                        0 -> {
                            "登陆成功"
                            // 保存token
                        }
                        86038 -> "二维码失效"
                        86090 -> "已扫描未确认"
                        86101 -> "未扫码"
                    }
                }
            }
        }*/
    }

    suspend fun refreshQrcode(): String {
        return "loginRepository.getQrcode().data?.url"
    }
}