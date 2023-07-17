package com.anpe.bilibiliandyou.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.anpe.bilibiliandyou.network.data.model.hotWord.Data
import com.anpe.bilibiliandyou.entity.suggest.SuggestEntity
import com.anpe.bilibiliandyou.entity.suggest.SuggestRootEntity
import com.anpe.bilibiliandyou.entity.userCard.UserCardEntity
import com.anpe.bilibiliandyou.entity.view.ViewEntity
import com.google.gson.stream.JsonReader
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.EnumMap
import java.util.Locale


class Utils {
    companion object {
        fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = composed {
            clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
        }

        fun Long.milliToDateString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
            val date = Date(this)
            return SimpleDateFormat(pattern, Locale.CHINA).format(date)
        }

        fun Long.secondToDateString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
            val date = Date(this * 1000)
            return SimpleDateFormat(pattern, Locale.CHINA).format(date)
        }

        fun Int.secondToDateString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
            val date = Date(this.toLong() * 1000)
            return SimpleDateFormat(pattern, Locale.CHINA).format(date)
        }

        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        fun getStatusBarHeight(context: Context): Dp {
            val resId = context.resources.getIdentifier(
                "status_bar_height", "dimen", "android"
            )
            val pxHeight = context.resources.getDimensionPixelSize(resId)
            val scale = context.resources.displayMetrics.density
            return (pxHeight / scale + 0.5f).toInt().dp
        }

        private fun test(context: Context) {
            val inputStream = context.assets.open("HotVideo.json")
            val inputStreamReader = InputStreamReader(inputStream)
            val jsonReader = JsonReader(inputStreamReader)
//            val entity: HotVideoEntity = gson.fromJson(jsonReader, HotVideoEntity::class.java)
        }

        fun Float.pxToDp(context: Context): Float {
            val density = context.resources.displayMetrics.density;
            return (this / density * if (this >= 0) 1 else -1)
        }

        fun interval(width: Int): Int = when {
            width < 500 -> 2
            width < 800 -> 3
            else -> 4
        }

        fun Int.toSimplify(): String {
            return if (this > 1000000) {
//                "${this / 1000000}M"
                "%.1f".format(this / 1000000f) + "M"
            } else if (this > 1000) {
                "%.1f".format(this / 1000f) + "K"
//                "${this / 1000}K"
            } else {
                "$this"
            }
        }

        // 生成二维码
        fun String.generateQrcode(
            width: Int = 400,
            height: Int = 400,
            logoBitmap: Bitmap? = null,
            onColor: Int = -14575885,
            offColor: Int = -0x1
        ): Bitmap {
            return createQRCode(this, width, height, logoBitmap, onColor, offColor)
        }

        /**
         * 创建二维码
         *
         * @param content   content
         * @param widthPix  widthPix
         * @param heightPix heightPix
         * @param logoBm    logoBm
         * @return 二维码
         */
        fun createQRCode(
            content: String,
            widthPix: Int,
            heightPix: Int,
            logoBitmap: Bitmap?,
            onColor: Int = -0x1,
            offColor: Int = -14575885
        ): Bitmap {
            // 配置参数
            val hints: MutableMap<EncodeHintType, Any?> =
                EnumMap(com.google.zxing.EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"

            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(
                content, BarcodeFormat.QR_CODE, widthPix,
                heightPix, hints
            )
            val pixels = IntArray(widthPix * heightPix)

            // 下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix[x, y]) {
                        pixels[y * widthPix + x] = onColor
                    } else {
                        pixels[y * widthPix + x] = offColor
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)

            logoBitmap?.let {
                bitmap = addLogo(bitmap, logoBitmap)
            }

            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap
        }

        /**
         * 在二维码中间添加Logo图案
         */
        private fun addLogo(src: Bitmap, logo: Bitmap): Bitmap? {
            // 获取图片的宽高
            val srcWidth = src.width
            val srcHeight = src.height
            val logoWidth = logo.width
            val logoHeight = logo.height

            if (srcWidth == 0 || srcHeight == 0) {
                return null
            }
            if (logoWidth == 0 || logoHeight == 0) {
                return src
            }

            // logo大小为二维码整体大小的1/5
            val scaleFactor = srcWidth * 1.0f / 5 / logoWidth
            var bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
            try {
                val canvas = Canvas(bitmap!!)
                canvas.drawBitmap(src, 0f, 0f, null)
                canvas.scale(
                    scaleFactor,
                    scaleFactor,
                    (srcWidth / 2).toFloat(),
                    (srcHeight / 2).toFloat()
                )
                canvas.drawBitmap(
                    logo,
                    ((srcWidth - logoWidth) / 2).toFloat(),
                    ((srcHeight - logoHeight) / 2).toFloat(),
                    null
                )
                canvas.save()
                canvas.restore()
            } catch (e: Exception) {
                bitmap = null
                e.stackTrace
            }
            return bitmap
        }
/*

        fun SuggestEntity.toSuggestRootEntity(): SuggestRootEntity {
            val list = mutableListOf<Data>()

            list.add(0, `0`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(1, `1`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(2, `2`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(3, `3`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(4, `4`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(5, `5`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(6, `6`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(7, `7`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })
            list.add(8, `8`.let {
                Data(it.name, it.ref, it.spid, it.term, it.value)
            })

            return SuggestRootEntity(list)
        }
*/

        fun details(viewEntity: ViewEntity, userCardEntity: UserCardEntity) {
            if (viewEntity.data != null && userCardEntity.data != null) {

            }
        }

        fun String.fromHtmlToString(flag: Int = HtmlCompat.FROM_HTML_MODE_LEGACY): String {
            return HtmlCompat.fromHtml(this, flag).toString()
        }

        /**
         * 汉字转拼音首字母，用tinypinyin实现
         */
        fun String.toPinyinFirstCharacter(): String {
            var returnString = ""
            this.forEach {
//            returnString += Pinyin.toPinyin(it).toLowerCase()[0]
            }
            return returnString
        }
    }
}