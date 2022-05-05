package com.imzqqq.app.features.crypto.recover

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.nulabinc.zxcvbn.Strength
import com.imzqqq.app.core.platform.WaitingViewData
import org.matrix.android.sdk.api.session.securestorage.SsssKeyCreationInfo

data class BootstrapViewState(
        val setupMode: SetupMode,
        val step: BootstrapStep = BootstrapStep.CheckingMigration,
        val passphrase: String? = null,
        val migrationRecoveryKey: String? = null,
        val passphraseRepeat: String? = null,
        val crossSigningInitialization: Async<Unit> = Uninitialized,
        val passphraseStrength: Async<Strength> = Uninitialized,
        val passphraseConfirmMatch: Async<Unit> = Uninitialized,
        val recoveryKeyCreationInfo: SsssKeyCreationInfo? = null,
        val initializationWaitingViewData: WaitingViewData? = null,
        val recoverySaveFileProcess: Async<Unit> = Uninitialized
) : MavericksState {

    constructor(args: BootstrapBottomSheet.Args) : this(setupMode = args.setUpMode)
}
