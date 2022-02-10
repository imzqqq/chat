package com.imzqqq.app.features.home.room.detail.timeline.url

import org.matrix.android.sdk.api.session.media.PreviewUrlData

/**
 * The state representing a preview url UI state for an Event
 */
sealed class PreviewUrlUiState {
    // No info
    object Unknown : PreviewUrlUiState()

    // The event does not contain any URLs
    object NoUrl : PreviewUrlUiState()

    // Loading
    object Loading : PreviewUrlUiState()

    // Error
    data class Error(val throwable: Throwable) : PreviewUrlUiState()

    // PreviewUrl data
    data class Data(val eventId: String,
                    val url: String,
                    val previewUrlData: PreviewUrlData) : PreviewUrlUiState()
}
