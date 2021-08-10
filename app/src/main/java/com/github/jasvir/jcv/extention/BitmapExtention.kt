package com.github.jasvir.jcv.extention

import android.graphics.Bitmap
import android.graphics.Matrix


object BitmapExtention {

    private fun fixOrientation(bitmap: Bitmap): Float {
        return if (bitmap.width > bitmap.height) {
            90F
        } else 0F
    }

    fun Bitmap.flipIMage() : Bitmap{
        val matrix = Matrix()
        val rotation = fixOrientation(this)
        matrix.postRotate(rotation)
        matrix.preScale(-1F, 1F)
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }


}