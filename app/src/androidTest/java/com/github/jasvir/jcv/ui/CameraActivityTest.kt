package com.github.jasvir.jcv.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.jasvir.jcv.R
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class CameraActivityTest {

    @get:Rule
    var mainActivityTestRule: ActivityScenarioRule<CameraActivity> =
        ActivityScenarioRule(CameraActivity::class.java)


    @Test
    fun test_camera_views_displayed(){
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.isDisplayed(),
                withId(R.id.preview_view)
            ))


        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.isDisplayed(),
                withId(R.id.switch_camera_button)
            ))

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.isDisplayed(),
                withId(R.id.take_picture_button)
            ))

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.isDisplayed(),
                withId(R.id.control_flash_button)
            ))


    }






}