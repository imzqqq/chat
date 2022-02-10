package com.imzqqq.app.core.di

import android.os.Handler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventControllerHandler
import com.imzqqq.app.features.home.room.detail.timeline.helper.TimelineAsyncHelper

@Module
@InstallIn(ActivityComponent::class)
object HomeModule {

    @Provides
    @JvmStatic
    @TimelineEventControllerHandler
    fun providesTimelineBackgroundHandler(): Handler {
        return TimelineAsyncHelper.getBackgroundHandler()
    }
}
