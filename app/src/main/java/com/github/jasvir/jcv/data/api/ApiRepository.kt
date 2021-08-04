package com.github.jasvir.jcv.data.api

import com.github.jasvir.jcv.data.data_classes.UploadResponse

import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response

/**
 * ApiRepository
 *
 * Created by jasvir on 01-Feb-2020 at 11:45 AM.
 */
class ApiRepository(private val apiService: ApiService) : ApiRepo {
    override fun uploadPhotoAsync(photo: File): Deferred<Response<UploadResponse>> {
        val imageRequestFile =
            photo.asRequestBody("application/x-www-form-urlencoded".toMediaType())

        val photoPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            photo.absolutePath,
            imageRequestFile
        )

        return apiService.uploadPhotoAsync("Basic YWNjX2JjOWFkYTEyMzI3MjhjMToxZWY4NTVkMDhlZDAwNzcyZjcwNDVmNjUyMDMyMmQwNA==",photoPart)
    }
}