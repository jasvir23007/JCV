package com.github.jasvir.jcv.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import com.github.jasvir.jcv.base.BaseTestClass
import com.github.jasvir.jcv.data.api.ApiRepo
import com.github.jasvir.jcv.data.data_classes.UploadResponse
import com.squareup.moshi.Moshi
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.koin.test.get
import retrofit2.Response
import java.io.File
import kotlin.time.ExperimentalTime


class CameraViewModelTest : BaseTestClass() {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val image:File = mockk()
    private lateinit var viewModel: CameraViewModel
    private val apiRepo: ApiRepo = mockk()
    private val moshi: Moshi = mockk()


    @ExperimentalTime
    @Before
    fun setUp() {
        super.before()
        viewModel = CameraViewModel(apiRepo,moshi)
    }

    @After
    fun tearDown() {
    super.after()
    }



    @Test
    fun test_capture_image_result() {
        viewModel.setOnImageCapturedResult("/img/test.jpg")
        assertNotNull(viewModel._onImageCapturedResult.value)
    }


    @Test
    fun test_switch_camera_facing_lense() {
        viewModel.switchCameraFacingLense(CameraSelector.LENS_FACING_FRONT)
        assertEquals(viewModel._cameraConfig.value?.lens,CameraSelector.LENS_FACING_FRONT)
    }

    @Test
    fun test_switch_flash_mode() {
        viewModel.switchFlashMode(ImageCapture.FLASH_MODE_ON)
        assertEquals(viewModel._cameraConfig.value?.flash,ImageCapture.FLASH_MODE_ON)

    }

    @Test
    fun uploadPhoto() = runBlocking {
        val mockResponse = mockk<Response<UploadResponse>>(relaxed = true)
        val response = coEvery { apiRepo.uploadPhotoAsync(image) } returns CompletableDeferred(mockResponse)
        assertNotNull(response)
    }
}