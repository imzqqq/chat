package com.imzqqq.app.features.crypto.verification

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for the verification bottom sheet
 */
sealed class VerificationBottomSheetViewEvents : VectorViewEvents {
    object Dismiss : VerificationBottomSheetViewEvents()
    object AccessSecretStore : VerificationBottomSheetViewEvents()
    object GoToSettings : VerificationBottomSheetViewEvents()
    data class ModalError(val errorMessage: CharSequence) : VerificationBottomSheetViewEvents()
}
