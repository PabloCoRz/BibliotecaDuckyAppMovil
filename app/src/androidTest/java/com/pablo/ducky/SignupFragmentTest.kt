package com.pablo.ducky

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pablo.ducky.ui.auth.SignupFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignupFragmentTest {

    @Test
    fun signupFragment_campoEmailEsVisible() {
        launchFragmentInContainer<SignupFragment>(themeResId = R.style.Theme_Ducky)
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
    }
}
