package com.imzqqq.app.flow.di

import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.imzqqq.app.ChatApplication
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.appstore.EventHubImpl
import com.imzqqq.app.flow.components.notifications.NotificationFetcher
import com.imzqqq.app.flow.components.notifications.NotificationWorkerFactory
import com.imzqqq.app.flow.components.notifications.Notifier
import com.imzqqq.app.flow.components.notifications.SystemNotifier
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.db.AppDatabase
import com.imzqqq.app.flow.db.Converters
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.network.TimelineCases
import com.imzqqq.app.flow.network.TimelineCasesImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlowModule {

    @Provides
    @Singleton
    @JvmStatic
    fun providesAccountManager(db : AppDatabase) : AccountManager = AccountManager(db);

    @Provides
    @JvmStatic
    fun providesNotificationWorkerFactory(
        notificationsFetcher: NotificationFetcher
    ): NotificationWorkerFactory = NotificationWorkerFactory(notificationsFetcher)

    @Provides
    @JvmStatic
    fun providesNotificationFetcher(mastodonApi: MastodonApi,
                                    accountManager: AccountManager,
                                    notifier: Notifier
    ): NotificationFetcher = NotificationFetcher(mastodonApi, accountManager, notifier)

    @Provides
    @JvmStatic
    fun providesBroadcastManager(app: ChatApplication): LocalBroadcastManager = LocalBroadcastManager.getInstance(app)

    @Provides
    @JvmStatic
    fun providesTimelineUseCases(
            api: MastodonApi,
            eventHub: EventHub
    ): TimelineCases = TimelineCasesImpl(api, eventHub)

    @Provides
    @JvmStatic
    @Singleton
    fun providesEventHub(): EventHub = EventHubImpl

    @Provides
    @JvmStatic
    @Singleton
    fun providesAppDatabase(
        appContext: Context, converters: Converters
    ): AppDatabase = Room.databaseBuilder(
        appContext, AppDatabase::class.java, "flowDB"
    ).addTypeConverter(converters)
        .allowMainThreadQueries()
        .addMigrations(
            AppDatabase.MIGRATION_2_3,   AppDatabase.MIGRATION_3_4,   AppDatabase.MIGRATION_4_5,
            AppDatabase.MIGRATION_5_6,   AppDatabase.MIGRATION_6_7,   AppDatabase.MIGRATION_7_8,
            AppDatabase.MIGRATION_8_9,   AppDatabase.MIGRATION_9_10,  AppDatabase.MIGRATION_10_11,
            AppDatabase.MIGRATION_11_12, AppDatabase.MIGRATION_12_13, AppDatabase.MIGRATION_10_13,
            AppDatabase.MIGRATION_13_14, AppDatabase.MIGRATION_14_15, AppDatabase.MIGRATION_15_16,
            AppDatabase.MIGRATION_16_17, AppDatabase.MIGRATION_17_18, AppDatabase.MIGRATION_18_19,
            AppDatabase.MIGRATION_19_20, AppDatabase.MIGRATION_20_21, AppDatabase.MIGRATION_21_22,
            AppDatabase.MIGRATION_22_23, AppDatabase.MIGRATION_23_24, AppDatabase.MIGRATION_24_25,
            AppDatabase.MIGRATION_26_27,
            AppDatabase.Migration25_26(appContext.getExternalFilesDir("downloads"))
        ).build()

    @Provides
    @Singleton
    @JvmStatic
    fun providesNotifier(context: Context): Notifier = SystemNotifier(context)
}
