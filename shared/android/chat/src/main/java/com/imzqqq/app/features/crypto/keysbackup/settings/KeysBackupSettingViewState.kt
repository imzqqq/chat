package com.imzqqq.app.features.crypto.keysbackup.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.crypto.keysbackup.KeysBackupState
import org.matrix.android.sdk.internal.crypto.keysbackup.model.KeysBackupVersionTrust
import org.matrix.android.sdk.internal.crypto.keysbackup.model.rest.KeysVersionResult

data class KeysBackupSettingViewState(val keysBackupVersionTrust: Async<KeysBackupVersionTrust> = Uninitialized,
                                      val keysBackupState: KeysBackupState? = null,
                                      val keysBackupVersion: KeysVersionResult? = null,
                                      val deleteBackupRequest: Async<Unit> = Uninitialized) :
        MavericksState
