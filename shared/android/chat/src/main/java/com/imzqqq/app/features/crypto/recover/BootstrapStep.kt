package com.imzqqq.app.features.crypto.recover

/**
 * TODO The schema is not up to date
 *
 *                        ┌───────────────────────────────────┐
 *                        │  BootstrapStep.SetupSecureBackup  │
 *                        └───────────────────────────────────┘
 *                                          │
 *                                          │
 *                                          ▼
 *                             ┌─────────────────────────┐
 *                             │ User has signing keys?  │──────────── Account
 *                             └─────────────────────────┘            Creation ?
 *                                          │                              │
 *                                          No                             │
 *                                          │                              │
 *                                          │                              │
 *                                          ▼                              │
 *                        ┌───────────────────────────────────┐            │
 *                        │  BootstrapStep.CheckingMigration  │            │
 *                        └───────────────────────────────────┘            │
 *                                          │                              │
 *                                          │                              │
 *                           Existing       ├─────────No ───────┐          │
 *                     ┌────Keybackup───────┘     KeyBackup     │          │
 *                     │                                        │          │
 *                     │                                        ▼          ▼
 *                     ▼                                    ┌────────────────────────────────────┐
 *     ┌─────────────────────────────────────────┐          │   BootstrapStep.SetupPassphrase    │◀─┐
 *     │BootstrapStep.GetBackupSecretForMigration│          └────────────────────────────────────┘  │
 *     └─────────────────────────────────────────┘                             │                    │
 *                             │                                               │                 ┌Back
 *                             │                                               ▼                 │
 *                             │                            ┌────────────────────────────────────┤
 *                             │                            │  BootstrapStep.ConfirmPassphrase   │──┐
 *                             │                            └────────────────────────────────────┘  │
 *                             │                                               │                    │
 *                             │                                      is password/reauth needed?    │
 *                             │                                               │                    │
 *                             │                                               ▼                    │
 *                             │                            ┌────────────────────────────────────┐  │
 *                             │                            │   BootstrapStep.AccountReAuth      │  │
 *                             │                            └────────────────────────────────────┘  │
 *                             │                                               │                    │
 *                             │                                               │                    │
 *                             │                            ┌──────────────────┘         password not needed (in
 *                             │                            │                                    memory)
 *                             │                            │                                       │
 *                             │                            ▼                                       │
 *                             │         ┌────────────────────────────────────┐                     │
 *                             └────────▶│     BootstrapStep.Initializing     │◀────────────────────┘
 *                                       └────────────────────────────────────┘
 *                                                          │
 *                                                          │
 *                                                          │
 *                                                          ▼
 *                                        ┌────────────────────────────────────┐
 *                                        │   BootstrapStep.SaveRecoveryKey    │
 *                                        └────────────────────────────────────┘
 *                                                          │
 *                                                          │
 *                                                          │
 *                                                          ▼
 *                                       ┌────────────────────────────────────────┐
 *                                       │       BootstrapStep.DoneSuccess        │
 *                                       └────────────────────────────────────────┘
 *
 */

sealed class BootstrapStep {
    // This is the first step
    object CheckingMigration : BootstrapStep()

    // Use will be asked to choose between passphrase or recovery key, or to start process if a key backup exists
    data class FirstForm(val keyBackUpExist: Boolean, val reset: Boolean = false) : BootstrapStep()

    object SetupPassphrase : BootstrapStep()
    object ConfirmPassphrase : BootstrapStep()

    data class AccountReAuth(val failure: String? = null) : BootstrapStep()

    abstract class GetBackupSecretForMigration : BootstrapStep()
    data class GetBackupSecretPassForMigration(val useKey: Boolean) : GetBackupSecretForMigration()
    object GetBackupSecretKeyForMigration : GetBackupSecretForMigration()

    object Initializing : BootstrapStep()
    data class SaveRecoveryKey(val isSaved: Boolean) : BootstrapStep()
    object DoneSuccess : BootstrapStep()
}

fun BootstrapStep.GetBackupSecretForMigration.useKey(): Boolean {
    return when (this) {
        is BootstrapStep.GetBackupSecretPassForMigration -> useKey
        else                                             -> true
    }
}
