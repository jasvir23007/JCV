package com.github.jasvir.jcv.data.api

import com.github.jasvir.jcv.data.data_classes.UploadResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * ApiService
 *
 * Created by jasvir on 01-Feb-2020 at 11:45 AM.
 */
interface ApiService {
    @Multipart
    @POST("faces/detections")
    fun uploadPhotoAsync(@Header("Authorization")  auth:String = "Basic YWNjX2JjOWFkYTEyMzI3MjhjMToxZWY4NTVkMDhlZDAwNzcyZjcwNDVmNjUyMDMyMmQwNA==", @Part photo: MultipartBody.Part): Deferred<Response<UploadResponse>>
}