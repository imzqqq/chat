package com.imzqqq.app.features.media

import com.github.piasy.biv.loader.ImageLoader
import java.io.File

interface DefaultImageLoaderCallback : ImageLoader.Callback {

    override fun onFinish() {
        // no-op
    }

    override fun onSuccess(image: File?) {
        // no-op
    }

    override fun onFail(error: Exception?) {
        // no-op
    }

    override fun onCacheHit(imageType: Int, image: File?) {
        // no-op
    }

    override fun onCacheMiss(imageType: Int, image: File?) {
        // no-op
    }

    override fun onProgress(progress: Int) {
        // no-op
    }

    override fun onStart() {
        // no-op
    }
}
