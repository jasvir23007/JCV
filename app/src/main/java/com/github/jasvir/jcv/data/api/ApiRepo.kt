package com.github.jasvir.jcv.data.api

import com.github.jasvir.jcv.data.data_classes.UploadResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.File

/**
 * ApiRepo
 *
 * Created by jasvir on 01-Feb-2020 at 11:45 AM.
 */
interface ApiRepo {
    fun uploadPhotoAsync(photo: File): Deferred<Response<UploadResponse>>
}