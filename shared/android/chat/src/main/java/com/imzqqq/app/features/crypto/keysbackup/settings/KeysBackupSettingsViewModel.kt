package com.imzqqq.app.features.crypto.keysbackup.settings

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import org.matrix.android.sdk.api.MatrixCallback
import org.matrix.android.sdk.api.NoOpMatrixCallback
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.crypto.keysbackup.KeysBackupService
import org.matrix.android.sdk.api.session.crypto.keysbackup.KeysBackupState
import org.matrix.android.sdk.api.session.crypto.keysbackup.KeysBackupStateListener
import org.matrix.android.sdk.internal.crypto.keysbackup.model.KeysBackupVersionTrust

class KeysBackupSettingsViewModel @AssistedInject constructor(@Assisted initialState: KeysBackupSettingViewState,
                                                              session: Session
) : VectorViewModel<KeysBackupSettingViewState, KeyBackupSettingsAction, EmptyViewEvents>(initialState),
        KeysBackupStateListener {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<KeysBackupSettingsViewModel, KeysBackupSettingViewState> {
        override fun create(initialState: KeysBackupSettingViewState): KeysBackupSettingsViewModel
    }

    companion object : MavericksViewModelFactory<KeysBackupSettingsViewModel, KeysBackupSettingViewState> by hiltMavericksViewModelFactory()

    private val keysBackupService: KeysBackupService = session.cryptoService().keysBackupService()

    init {
        setState {
            this.copy(
                    keysBackupState = keysBackupService.state,
                    keysBackupVersion = keysBackupService.keysBackupVersion
            )
        }
        keysBackupService.addListener(this)
        getKeysBackupTrust()
    }

    override fun handle(action: KeyBackupSettingsAction) {
        when (action) {
            KeyBackupSettingsAction.Init              -> init()
            KeyBackupSettingsAction.GetKeyBackupTrust -> getKeysBackupTrust()
            KeyBackupSettingsAction.DeleteKeyBackup   -> deleteCurrentBackup()
        }
    }

    private fun init() {
        keysBackupService.forceUsingLastVersion(NoOpMatrixCallback())
    }

    private fun getKeysBackupTrust() = withState { state ->
        val versionResult = keysBackupService.keysBackupVersion

        if (state.keysBackupVersionTrust is Uninitialized && versionResult != null) {
            setState {
                copy(
                        keysBackupVersionTrust = Loading(),
                        deleteBackupRequest = Uninitialized
                )
            }

            keysBackupService
                    .getKeysBackupTrust(versionResult, object : MatrixCallback<KeysBackupVersionTrust> {
                        override fun onSuccess(data: KeysBackupVersionTrust) {
                            setState {
                                copy(
                                        keysBackupVersionTrust = Success(data)
                                )
                            }
                        }

                        override fun onFailure(failure: Throwable) {
                            setState {
                                copy(
                                        keysBackupVersionTrust = Fail(failure)
                                )
                            }
                        }
                    })
        }
    }

    override fun onCleared() {
        keysBackupService.removeListener(this)
        super.onCleared()
    }

    override fun onStateChange(newState: KeysBackupState) {
        setState {
            copy(
                    keysBackupState = newState,
                    keysBackupVersion = keysBackupService.keysBackupVersion
            )
        }

        getKeysBackupTrust()
    }

    private fun deleteCurrentBackup() {
        val keysBackupService = keysBackupService

        if (keysBackupService.currentBackupVersion != null) {
            setState {
                copy(
                        deleteBackupRequest = Loading()
                )
            }

            keysBackupService.deleteBackup(keysBackupService.currentBackupVersion!!, object : MatrixCallback<Unit> {
                override fun onSuccess(data: Unit) {
                    setState {
                        copy(
                                keysBackupVersion = null,
                                keysBackupVersionTrust = Uninitialized,
                                // We do not care about the success data
                                deleteBackupRequest = Uninitialized
                        )
                    }
                }

                override fun onFailure(failure: Throwable) {
                    setState {
                        copy(
                                deleteBackupRequest = Fail(failure)
                        )
                    }
                }
            })
        }
    }

    fun canExit(): Boolean {
        val currentBackupState = keysBackupService.state

        return currentBackupState == KeysBackupState.Unknown ||
                currentBackupState == KeysBackupState.CheckingBackUpOnHomeserver
    }
}
