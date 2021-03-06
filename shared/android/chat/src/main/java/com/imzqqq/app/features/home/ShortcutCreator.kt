package com.imzqqq.app.features.home

import android.content.Context
import android.content.pm.ShortcutInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.imzqqq.app.BuildConfig
import com.imzqqq.app.core.glide.GlideApp
import com.imzqqq.app.core.utils.DimensionConverter
import com.imzqqq.app.features.home.room.detail.RoomDetailActivity
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

private val useAdaptiveIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
private const val adaptiveIconSizeDp = 108
private const val adaptiveIconOuterSidesDp = 18
private const val directShareCategory = BuildConfig.APPLICATION_ID + ".SHORTCUT_SHARE"

class ShortcutCreator @Inject constructor(
        private val context: Context,
        private val avatarRenderer: AvatarRenderer,
        private val dimensionConverter: DimensionConverter
) {
    private val adaptiveIconSize = dimensionConverter.dpToPx(adaptiveIconSizeDp)
    private val adaptiveIconOuterSides = dimensionConverter.dpToPx(adaptiveIconOuterSidesDp)
    private val iconSize by lazy {
        if (useAdaptiveIcon) {
            adaptiveIconSize - (adaptiveIconOuterSides * 2)
        } else {
            dimensionConverter.dpToPx(72)
        }
    }

    fun canCreateShortcut(): Boolean {
        return ShortcutManagerCompat.isRequestPinShortcutSupported(context)
    }

    @WorkerThread
    fun create(roomSummary: RoomSummary, rank: Int = 1): ShortcutInfoCompat {
        val intent = RoomDetailActivity.shortcutIntent(context, roomSummary.roomId)
        val bitmap = try {
            avatarRenderer.shortcutDrawable(GlideApp.with(context), roomSummary.toMatrixItem(), iconSize)
        } catch (failure: Throwable) {
            null
        }
        val categories = if (Build.VERSION.SDK_INT >= 25) {
            setOf(directShareCategory, ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION)
        } else {
            setOf(directShareCategory)
        }

        return ShortcutInfoCompat.Builder(context, roomSummary.roomId)
                .setShortLabel(roomSummary.displayName)
                .setIcon(bitmap?.toProfileImageIcon())
                .setIntent(intent)
                .setLongLived(true)
                .setRank(rank)
                .setCategories(categories)
                .build()
    }

    private fun Bitmap.toProfileImageIcon(): IconCompat {
        return if (useAdaptiveIcon) {
            val insetBmp = Bitmap.createBitmap(adaptiveIconSize, adaptiveIconSize, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(insetBmp)
            canvas.drawBitmap(this, adaptiveIconOuterSides.toFloat(), adaptiveIconOuterSides.toFloat(), null)

            IconCompat.createWithAdaptiveBitmap(insetBmp)
        } else {
            IconCompat.createWithBitmap(this)
        }
    }
}
