package com.imzqqq.app.features.home.room.detail.timeline.helper

import com.imzqqq.app.core.resources.UserPreferencesProvider
import org.matrix.android.sdk.api.session.room.timeline.TimelineSettings
import javax.inject.Inject

class TimelineSettingsFactory @Inject constructor(private val userPreferencesProvider: UserPreferencesProvider) {

    fun create(): TimelineSettings {
        return TimelineSettings(
                initialSize = 30,
                buildReadReceipts = userPreferencesProvider.shouldShowReadReceipts())
    }
}
