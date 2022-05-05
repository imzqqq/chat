package com.imzqqq.app.features.crypto.keysbackup.settings

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class KeyBackupSettingsAction : VectorViewModelAction {
    object Init : KeyBackupSettingsAction()
    object GetKeyBackupTrust : KeyBackupSettingsAction()
    object DeleteKeyBackup : KeyBackupSettingsAction()
}
