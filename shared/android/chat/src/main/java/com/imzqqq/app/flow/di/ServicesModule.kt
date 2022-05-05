package com.imzqqq.app.flow.di

import android.app.Service
import android.content.Context
import dagger.Binds
import com.imzqqq.app.flow.service.SendTootService
import com.imzqqq.app.flow.service.ServiceClient
import com.imzqqq.app.flow.service.ServiceClientImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ServicesModule {

    @Binds
    fun contributesSendTootService(sendTootService : SendTootService): Service

    companion object {
        @Provides
        @JvmStatic
        fun providesServiceClient(context: Context): ServiceClient = ServiceClientImpl(context)
    }
}
