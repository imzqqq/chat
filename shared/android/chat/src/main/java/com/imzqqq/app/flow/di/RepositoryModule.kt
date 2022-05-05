package com.imzqqq.app.flow.di

import com.google.gson.Gson
import com.imzqqq.app.flow.components.timeline.TimelineRepository
import com.imzqqq.app.flow.components.timeline.TimelineRepositoryImpl
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.db.AppDatabase
import com.imzqqq.app.flow.network.MastodonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @JvmStatic
    fun providesTimelineRepository(
            db: AppDatabase,
            mastodonApi: MastodonApi,
            accountManager: AccountManager,
            gson: Gson
    ): TimelineRepository = TimelineRepositoryImpl(db.timelineDao(), mastodonApi, accountManager, gson)
}
