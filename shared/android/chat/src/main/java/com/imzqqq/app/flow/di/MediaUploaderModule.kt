package com.imzqqq.app.flow.di

import android.content.Context
import com.imzqqq.app.flow.components.compose.MediaUploader
import com.imzqqq.app.flow.components.compose.MediaUploaderImpl
import com.imzqqq.app.flow.network.MastodonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class MediaUploaderModule {

    @Provides
    fun providesMediaUploader(
        context: Context, mastodonApi: MastodonApi
    ): MediaUploader = MediaUploaderImpl(context, mastodonApi)
}
