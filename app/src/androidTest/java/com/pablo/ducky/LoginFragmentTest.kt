package com.pablo.ducky

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pablo.ducky.ui.auth.LoginFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @Test
    fun loginFragment_campoEmailEsVisible() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_Ducky)
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
    }

    @Test
    fun loginFragment_campoPasswordEsVisible() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_Ducky)
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
    }

    @Test
    fun loginFragment_botonLoginEsVisible() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_Ducky)
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    @Test
    fun loginFragment_botonCrearCuentaEsVisible() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_Ducky)
        onView(withId(R.id.btnGoSignup)).check(matches(isDisplayed()))
    }
}
