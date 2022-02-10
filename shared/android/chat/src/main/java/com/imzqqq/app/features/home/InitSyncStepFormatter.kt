package com.imzqqq.app.features.home

import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import org.matrix.android.sdk.api.session.initsync.InitSyncStep
import javax.inject.Inject

class InitSyncStepFormatter @Inject constructor(
        private val stringProvider: StringProvider
) {
    fun format(initSyncStep: InitSyncStep): String {
        return stringProvider.getString(
                when (initSyncStep) {
                    InitSyncStep.ServerComputing              -> R.string.initial_sync_start_server_computing
                    InitSyncStep.Downloading                  -> R.string.initial_sync_start_downloading
                    InitSyncStep.ImportingAccount             -> R.string.initial_sync_start_importing_account
                    InitSyncStep.ImportingAccountCrypto       -> R.string.initial_sync_start_importing_account_crypto
                    InitSyncStep.ImportingAccountRoom         -> R.string.initial_sync_start_importing_account_rooms
                    InitSyncStep.ImportingAccountGroups       -> R.string.initial_sync_start_importing_account_groups
                    InitSyncStep.ImportingAccountData         -> R.string.initial_sync_start_importing_account_data
                    InitSyncStep.ImportingAccountJoinedRooms  -> R.string.initial_sync_start_importing_account_joined_rooms
                    InitSyncStep.ImportingAccountInvitedRooms -> R.string.initial_sync_start_importing_account_invited_rooms
                    InitSyncStep.ImportingAccountLeftRooms    -> R.string.initial_sync_start_importing_account_left_rooms
                }
        )
    }
}
