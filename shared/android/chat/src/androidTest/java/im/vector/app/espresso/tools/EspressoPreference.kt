package com.imzqqq.app.espresso.tools

import android.widget.Switch
import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.PreferenceMatchers.withKey
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.imzqqq.app.R
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`

fun clickOnPreference(@StringRes textResId: Int) {
    onView(withId(R.id.recycler_view))
            .perform(actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(textResId)), click()))
}

fun clickOnSwitchPreference(preferenceKey: String) {
    onData(allOf(`is`(instanceOf(Preference::class.java)), withKey(preferenceKey)))
            .onChildView(withClassName(`is`(Switch::class.java.name))).perform(click())
}
