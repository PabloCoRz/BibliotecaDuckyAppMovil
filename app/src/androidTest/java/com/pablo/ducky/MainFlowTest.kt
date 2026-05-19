package com.pablo.ducky

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas instrumentales (Espresso) — se ejecutan en emulador o dispositivo real.
 *
 * Cubren los flujos principales de la app:
 *  1. Pantalla de login visible al arrancar
 *  2. Validación de campos vacíos en login
 *  3. Validación de email inválido en login
 *  4. Navegación exitosa al landing con credenciales válidas
 *  5. Navegación de login a signup
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainFlowTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // ─── Prueba 1: La pantalla de login es visible al iniciar la app ──────────

    @Test
    fun test1_loginScreenVisibleOnLaunch() {
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))
    }

    // ─── Prueba 2: Login con campos vacíos no navega ──────────────────────────

    @Test
    fun test2_loginWithEmptyFieldsShowsError() {
        // No escribimos nada, solo tocamos el botón
        onView(withId(R.id.btnLogin))
            .perform(click())

        // El campo email debe seguir visible (no navegamos)
        onView(withId(R.id.etEmail))
            .check(matches(isDisplayed()))
    }

    // ─── Prueba 3: Login con email inválido no navega ────────────────────────

    @Test
    fun test3_loginWithInvalidEmailShowsError() {
        onView(withId(R.id.etEmail))
            .perform(typeText("noesuncorreo"), closeSoftKeyboard())

        onView(withId(R.id.etPassword))
            .perform(typeText("123456"), closeSoftKeyboard())

        onView(withId(R.id.btnLogin))
            .perform(click())

        // La pantalla de login sigue visible
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))
    }

    // ─── Prueba 4: Login con credenciales válidas navega al landing ───────────

    @Test
    fun test4_loginWithValidCredentialsNavigatesToLanding() {
        onView(withId(R.id.etEmail))
            .perform(typeText("alumno@ducky.edu"), closeSoftKeyboard())

        onView(withId(R.id.etPassword))
            .perform(typeText("password123"), closeSoftKeyboard())

        onView(withId(R.id.btnLogin))
            .perform(click())

        // El landing debe aparecer (buscador del hero)
        onView(withId(R.id.etSearchLanding))
            .check(matches(isDisplayed()))
    }

    // ─── Prueba 5: Navegación de login → signup ────────────────────────────

    @Test
    fun test5_navigateFromLoginToSignup() {
        onView(withId(R.id.btnGoSignup))
            .perform(click())

        // El botón de signup debe ser visible
        onView(withId(R.id.btnSignup))
            .check(matches(isDisplayed()))
    }
}
