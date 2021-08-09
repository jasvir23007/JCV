package com.github.jasvir.jcv.base

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.test.KoinTest
import java.io.File
import kotlin.time.ExperimentalTime


open class BaseTestClass : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val application: Application = mockk()

    @ExperimentalTime
    @Before
    open fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)
        every { application.getString(any()) } returns "Test String"

    }



    /**
     * Reads input file and converts to json
     */
    fun getJson(path : String) : String {
        val uri = javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }


    @After
    open fun after() {
        Dispatchers.resetMain()
    }
}