package com.imzqqq.app.features.settings.crosssigning

import com.airbnb.mvrx.MavericksState
import org.matrix.android.sdk.api.session.crypto.crosssigning.MXCrossSigningInfo

data class CrossSigningSettingsViewState(
        val crossSigningInfo: MXCrossSigningInfo? = null,
        val xSigningIsEnableInAccount: Boolean = false,
        val xSigningKeysAreTrusted: Boolean = false,
        val xSigningKeyCanSign: Boolean = true
) : MavericksState
