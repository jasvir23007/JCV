package com.github.jasvir.jcv.ui

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.*
import com.github.jasvir.jcv.R
import com.github.jasvir.jcv.annotations.FlashMode
import com.github.jasvir.jcv.annotations.LensType
import com.github.jasvir.jcv.constants.Const.COORDINATES
import com.github.jasvir.jcv.constants.Const.IMG_FILE
import com.github.jasvir.jcv.data.api.ApiRepo
import com.github.jasvir.jcv.data.data_classes.UploadResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors


/**
 *
 * Copyright 2021 jasvir
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *            http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 **/
class CameraViewModel(
    private val apiRepository: ApiRepo,
    private val moshi: Moshi
) : ViewModel() {

    private val _cameraConfig = MutableLiveData<CameraConfig>()
    val cameraConfig: LiveData<CameraConfig>
        get() = _cameraConfig

    private val _onImageCapturedResult = MutableLiveData<String>()
    val onImageCapturedResult: LiveData<String>
        get() = _onImageCapturedResult

    val displayData = MutableLiveData<Bundle>()


    init {
        //Default Config
        _cameraConfig.value = CameraConfig(
            lens = CameraSelector.LENS_FACING_FRONT,
            flash = ImageCapture.FLASH_MODE_ON
        )
    }

    fun setOnImageCapturedResult(msg: String) {
        _onImageCapturedResult.postValue(msg)
    }

    fun switchCameraFacingLense(@LensType lens: Int) {
        val updatedConfig = _cameraConfig.value!!.copy(lens = lens)
        _cameraConfig.value = updatedConfig
    }

    fun switchFlashMode(@FlashMode mode: Int) {
        val updatedConfig = _cameraConfig.value!!.copy(flash = mode)
        _cameraConfig.value = updatedConfig
    }

    /**
     * Takes in current mode and displays icon to switch to alternative mode
     */
    @DrawableRes
    fun setFlashIcon(@FlashMode mode: Int): Int {
        return when (mode) {
            ImageCapture.FLASH_MODE_ON -> R.drawable.ic_flash_on_black_24dp
            ImageCapture.FLASH_MODE_OFF -> R.drawable.ic_flash_off_black_24dp
            ImageCapture.FLASH_MODE_AUTO -> R.drawable.ic_flash_auto_black_24dp
            else -> R.drawable.ic_flash_on_black_24dp
        }
    }

    fun takePicture(imageCapture: ImageCapture, parent: File) {

        val file = File(
            parent,
            "${System.currentTimeMillis()}.jpg"
        )
        imageCapture.takePicture(
            ImageCapture.OutputFileOptions.Builder(
                file
            )
                .build(),
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded ${file.path}"
                    Timber.d(msg)
                    setOnImageCapturedResult(msg)
                    uploadPhoto(file)
                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exception.message}"
                    Timber.e(msg)
                    setOnImageCapturedResult(msg)
                }
            }
        )
    }


    private fun uploadPhoto(photo: File) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val deferred = apiRepository.uploadPhotoAsync(photo)
                val response = deferred.await()

                launch(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        handleResponse(response.body(), photo)
                    } else {
                        handleException(
                            Exception("${response.errorBody()} Response was not successful.")
                        )
                    }
                }
            } catch (exception: Exception) {
                launch(Dispatchers.Main) {
                    handleException(exception)
                }
            }
        }
    }

    private fun handleResponse(response: UploadResponse?, photo: File) {
        response?.let {
            val jsonAdapter: JsonAdapter<UploadResponse> =
                moshi.adapter(UploadResponse::class.java)
            Timber.d("ProfileResponse: ${jsonAdapter.toJson(response)}")
            val bundle = Bundle()
            bundle.putString(IMG_FILE, photo.path)
            bundle.putParcelable(COORDINATES, response.result)
            displayData.value = bundle

        }
    }

    private fun handleException(exception: Exception) {
        Timber.e(exception)
    }


}

data class CameraConfig(val lens: Int, val flash: Int)