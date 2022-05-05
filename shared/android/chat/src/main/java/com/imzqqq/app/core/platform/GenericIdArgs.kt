package com.imzqqq.app.core.platform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Generic argument with one String. Can be an id (ex: roomId, spaceId, callId, etc.), or anything else
 */
@Parcelize
data class GenericIdArgs(
        val id: String
) : Parcelable
