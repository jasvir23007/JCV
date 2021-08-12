package com.github.jasvir.jcv.extention

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface


object BitmapExtention {
    private fun fixOrientation(photoPath: String): Float {
        val ei = ExifInterface(photoPath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                return 90F
            ExifInterface.ORIENTATION_ROTATE_180 ->
                return 180F
            ExifInterface.ORIENTATION_ROTATE_270 ->
                return 270F
            else ->
                return 0F
        }


    }


    fun Bitmap.flipIMage(path: String): Bitmap {
        val matrix = Matrix()
        val rotation = fixOrientation(path)
        if (rotation == 0f) {
            return this
        }
        matrix.postRotate(rotation)
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }


}