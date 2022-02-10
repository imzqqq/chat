package com.imzqqq.app.features.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface SettingsActivityPayload : Parcelable {

    @Parcelize object Root : SettingsActivityPayload
    @Parcelize object AdvancedSettings : SettingsActivityPayload
    @Parcelize object SecurityPrivacy : SettingsActivityPayload
    @Parcelize object SecurityPrivacyManageSessions : SettingsActivityPayload
    @Parcelize object General : SettingsActivityPayload
    @Parcelize object Notifications : SettingsActivityPayload

    @Parcelize
    data class DiscoverySettings(val expandIdentityPolicies: Boolean = false) : SettingsActivityPayload
}
