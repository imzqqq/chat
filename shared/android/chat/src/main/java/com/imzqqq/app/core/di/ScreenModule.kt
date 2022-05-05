package com.imzqqq.app.core.di

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import com.imzqqq.app.core.glide.GlideApp

@Module
@InstallIn(ActivityComponent::class)
object ScreenModule {

    @Provides
    @JvmStatic
    fun providesGlideRequests(context: AppCompatActivity) = GlideApp.with(context)

    @Provides
    @JvmStatic
    @ActivityScoped
    fun providesSharedViewPool() = RecyclerView.RecycledViewPool()
}
