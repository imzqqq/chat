package com.imzqqq.app.features.notifications

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitmapLoader @Inject constructor(private val context: Context) {

    /**
     * Avatar Url -> Bitmap
     */
    private val cache = HashMap<String, Bitmap?>()

    /**
     * Get icon of a room.
     * If already in cache, use it, else load it and call BitmapLoaderListener.onBitmapsLoaded() when ready
     */
    @WorkerThread
    fun getRoomBitmap(path: String?): Bitmap? {
        if (path == null) {
            return null
        }

        return cache.getOrPut(path) {
            loadRoomBitmap(path)
        }
    }

    @WorkerThread
    private fun loadRoomBitmap(path: String): Bitmap? {
        return path.let {
            try {
                Glide.with(context)
                        .asBitmap()
                        .load(path)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .submit()
                        .get()
            } catch (e: Exception) {
                Timber.e(e, "decodeFile failed")
                null
            }
        }
    }
}
