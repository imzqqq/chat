package com.imzqqq.app.flow.di

import android.content.BroadcastReceiver
import dagger.Binds
import com.imzqqq.app.flow.receiver.NotificationClearBroadcastReceiver
import com.imzqqq.app.flow.receiver.SendStatusBroadcastReceiver
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BroadcastReceiverModule {

    @Binds
    abstract fun contributeSendStatusBroadcastReceiver(
        sendStatusBroadcastReceiver : SendStatusBroadcastReceiver
    ): BroadcastReceiver

    @Binds
    abstract fun contributeNotificationClearBroadcastReceiver(
        notificationClearBroadcastReceiver : NotificationClearBroadcastReceiver
    ): BroadcastReceiver
}
