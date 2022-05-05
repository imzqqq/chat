package com.imzqqq.app.core.glide

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.imzqqq.app.ActiveSessionDataSource
import okhttp3.OkHttpClient
import java.io.InputStream

class FactoryUrl(private val activeSessionDataSource: ActiveSessionDataSource) : ModelLoaderFactory<GlideUrl, InputStream> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
        val client = activeSessionDataSource.currentValue?.orNull()?.getOkHttpClient() ?: OkHttpClient()
        return OkHttpUrlLoader(client)
    }

    override fun teardown() {
        // Do nothing, this instance doesn't own the client.
    }
}
