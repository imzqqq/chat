package com.imzqqq.app.core.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.imzqqq.app.core.dialogs.UnrecognizedCertificateDialog
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.features.call.webrtc.WebRtcCallManager
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.navigation.Navigator
import com.imzqqq.app.features.pin.PinLocker
import com.imzqqq.app.features.rageshake.BugReporter
import com.imzqqq.app.features.session.SessionListener
import com.imzqqq.app.features.settings.VectorPreferences
import com.imzqqq.app.features.ui.UiStateRepository
import kotlinx.coroutines.CoroutineScope

@InstallIn(SingletonComponent::class)
@EntryPoint
interface SingletonEntryPoint {

    fun sessionListener(): SessionListener

    fun avatarRenderer(): AvatarRenderer

    fun activeSessionHolder(): ActiveSessionHolder

    fun unrecognizedCertificateDialog(): UnrecognizedCertificateDialog

    fun navigator(): Navigator

    fun errorFormatter(): ErrorFormatter

    fun bugReporter(): BugReporter

    fun vectorPreferences(): VectorPreferences

    fun uiStateRepository(): UiStateRepository

    fun pinLocker(): PinLocker

    fun webRtcCallManager(): WebRtcCallManager

    fun appCoroutineScope(): CoroutineScope
}
