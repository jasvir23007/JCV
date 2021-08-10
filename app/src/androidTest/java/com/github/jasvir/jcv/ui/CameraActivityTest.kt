package com.github.jasvir.jcv.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.jasvir.jcv.R
import com.github.jasvir.jcv.ui.InstrumentedTestUtils.viewIsDisplayed
import org.hamcrest.Matchers
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
       onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.preview_view)
            ))


        onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.switch_camera_button)
            ))

        onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.take_picture_button)
            ))

        onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.control_flash_button)
            ))

    }



    @Test
    fun test_switch_camera_button(){
        onView(withId(R.id.switch_camera_button)).perform(click())
    }


    @Test
    fun test_switch_camera_flash_button(){
        if (viewIsDisplayed(R.id.control_flash_button))
        onView(withId(R.id.control_flash_button)).perform(click())
    }


    @Test
    fun test_capture_image_and_display(){
        onView(withId(R.id.take_picture_button)).perform(click())

        onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.progressBar)
            ))


        Thread.sleep(5000)

        onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.ivDisplayImg)
            ))

    }




}