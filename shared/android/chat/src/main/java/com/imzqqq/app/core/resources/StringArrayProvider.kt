package com.imzqqq.app.core.resources

import android.content.res.Resources
import androidx.annotation.ArrayRes
import androidx.annotation.NonNull
import javax.inject.Inject

class StringArrayProvider @Inject constructor(private val resources: Resources) {

    /**
     * Returns a localized string array from the application's package's
     * default string array table.
     *
     * @param resId Resource id for the string array
     * @return The string array associated with the resource, stripped of styled
     * text information.
     */
    @NonNull
    fun getStringArray(@ArrayRes resId: Int): Array<String> {
        return resources.getStringArray(resId)
    }
}
