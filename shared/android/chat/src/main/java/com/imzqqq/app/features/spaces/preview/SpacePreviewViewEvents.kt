package com.imzqqq.app.features.spaces.preview

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SpacePreviewViewEvents : VectorViewEvents {
    object Dismiss : SpacePreviewViewEvents()
    object JoinSuccess : SpacePreviewViewEvents()
    data class JoinFailure(val message: String?) : SpacePreviewViewEvents()
}
