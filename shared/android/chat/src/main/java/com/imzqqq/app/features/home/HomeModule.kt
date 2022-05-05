package com.imzqqq.app.features.home

import android.os.Handler
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventControllerHandler
import com.imzqqq.app.features.home.room.detail.timeline.helper.TimelineAsyncHelper

@DisableInstallInCheck
@Module
object HomeModule {

    @Provides
    @JvmStatic
    @TimelineEventControllerHandler
    fun providesTimelineBackgroundHandler(): Handler {
        return TimelineAsyncHelper.getBackgroundHandler()
    }
}
