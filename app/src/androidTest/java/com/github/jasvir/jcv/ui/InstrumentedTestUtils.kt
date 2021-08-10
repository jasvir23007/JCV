package com.github.jasvir.jcv.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

object InstrumentedTestUtils {

    fun viewIsDisplayed(viewId: Int): Boolean {
        val isDisplayed = booleanArrayOf(true)
        Espresso.onView(ViewMatchers.withId(viewId)).withFailureHandler { error, viewMatcher ->
            isDisplayed[0] = false
        }.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return isDisplayed[0]
    }
}