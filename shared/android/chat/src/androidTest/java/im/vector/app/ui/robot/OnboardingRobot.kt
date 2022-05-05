package com.imzqqq.app.ui.robot

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adevinta.android.barista.assertion.BaristaEnabledAssertions.assertDisabled
import com.adevinta.android.barista.assertion.BaristaEnabledAssertions.assertEnabled
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import com.imzqqq.app.R
import com.imzqqq.app.waitForView

class OnboardingRobot {

    fun createAccount(userId: String, password: String = "password", homeServerUrl: String = "http://10.0.2.2:8080") {
        initSession(true, userId, password, homeServerUrl)
    }

    fun login(userId: String, password: String = "password", homeServerUrl: String = "http://10.0.2.2:8080") {
        initSession(false, userId, password, homeServerUrl)
    }

    private fun initSession(createAccount: Boolean,
                            userId: String,
                            password: String,
                            homeServerUrl: String) {
        assertDisplayed(R.id.loginSplashSubmit, R.string.login_splash_submit)
        clickOn(R.id.loginSplashSubmit)
        assertDisplayed(R.id.loginServerTitle, R.string.login_server_title)
        // Chose custom server
        clickOn(R.id.loginServerChoiceOther)
        // Enter local synapse
        writeTo(R.id.loginServerUrlFormHomeServerUrl, homeServerUrl)
        assertEnabled(R.id.loginServerUrlFormSubmit)
        closeSoftKeyboard()
        clickOn(R.id.loginServerUrlFormSubmit)
        onView(isRoot()).perform(waitForView(withId(R.id.loginSignupSigninSubmit)))

        if (createAccount) {
            // Click on the signup button
            assertDisplayed(R.id.loginSignupSigninSubmit)
            clickOn(R.id.loginSignupSigninSubmit)
        } else {
            // Click on the signin button
            assertDisplayed(R.id.loginSignupSigninSignIn)
            clickOn(R.id.loginSignupSigninSignIn)
        }

        // Ensure password flow supported
        assertDisplayed(R.id.loginField)
        assertDisplayed(R.id.passwordField)

        writeTo(R.id.loginField, userId)
        assertDisabled(R.id.loginSubmit)
        writeTo(R.id.passwordField, password)
        assertEnabled(R.id.loginSubmit)

        closeSoftKeyboard()
        clickOn(R.id.loginSubmit)
    }
}
