package com.keylesspalace.flow.di

import com.google.gson.Gson
import com.keylesspalace.flow.components.timeline.TimelineRepository
import com.keylesspalace.flow.components.timeline.TimelineRepositoryImpl
import com.keylesspalace.flow.db.AccountManager
import com.keylesspalace.flow.db.AppDatabase
import com.keylesspalace.flow.network.FlowApi
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun providesTimelineRepository(
        db: AppDatabase,
        flowApi: FlowApi,
        accountManager: AccountManager,
        gson: Gson
    ): TimelineRepository {
        return TimelineRepositoryImpl(db.timelineDao(), flowApi, accountManager, gson)
    }
}
