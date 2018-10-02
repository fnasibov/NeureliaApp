package com.nasibov.fakhri.neurelia.util

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface

fun Bitmap.getRotatedBitmap(exifInterface: ExifInterface): Bitmap {
    val rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
    val angle: Float = when (rotation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90F
        ExifInterface.ORIENTATION_ROTATE_180 -> 180F
        ExifInterface.ORIENTATION_ROTATE_270 -> 270F
        ExifInterface.ORIENTATION_NORMAL -> 0F
        else -> 0F
    }
    if (angle != 0F) {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    return this
}