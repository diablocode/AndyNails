package mx.udg.cucea.andynails.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun base64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val cleanStr = base64Str.trim()
        val decodedBytes = Base64.decode(cleanStr, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (_: Exception) {
        null
    }
}
