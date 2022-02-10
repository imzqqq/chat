package com.imzqqq.app.espresso.tools

import android.app.Activity
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import com.imzqqq.app.activityIdlingResource
import com.imzqqq.app.waitForView
import com.imzqqq.app.withIdlingResource
import org.hamcrest.Matcher

inline fun <reified T : Activity> waitUntilActivityVisible(noinline block: (() -> Unit) = {}) {
    withIdlingResource(activityIdlingResource(T::class.java), block)
}

fun waitUntilViewVisible(viewMatcher: Matcher<View>) {
    Espresso.onView(ViewMatchers.isRoot()).perform(waitForView(viewMatcher))
}
