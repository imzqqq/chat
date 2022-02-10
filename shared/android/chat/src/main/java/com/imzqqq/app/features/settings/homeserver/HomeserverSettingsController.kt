package com.imzqqq.app.features.settings.homeserver

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.errorWithRetryItem
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericWithValueItem
import com.imzqqq.app.features.discovery.settingsCenteredImageItem
import com.imzqqq.app.features.discovery.settingsInfoItem
import com.imzqqq.app.features.discovery.settingsSectionTitleItem
import com.imzqqq.app.features.settings.VectorPreferences
import org.matrix.android.sdk.api.federation.FederationVersion
import org.matrix.android.sdk.api.session.homeserver.HomeServerCapabilities
import org.matrix.android.sdk.api.session.homeserver.RoomVersionStatus
import javax.inject.Inject

class HomeserverSettingsController @Inject constructor(
        private val stringProvider: StringProvider,
        private val errorFormatter: ErrorFormatter,
        private val vectorPreferences: VectorPreferences
) : TypedEpoxyController<HomeServerSettingsViewState>() {

    var callback: Callback? = null

    interface Callback {
        fun retry()
    }

    override fun buildModels(data: HomeServerSettingsViewState?) {
        data ?: return
        val host = this

        buildHeader(data)
        buildCapabilities(data)
        when (val federationVersion = data.federationVersion) {
            is Loading,
            is Uninitialized ->
                loadingItem {
                    id("loading")
                }
            is Fail          ->
                errorWithRetryItem {
                    id("error")
                    text(host.errorFormatter.toHumanReadable(federationVersion.error))
                    listener { host.callback?.retry() }
                }
            is Success       ->
                buildFederationVersion(federationVersion())
        }
    }

    private fun buildHeader(state: HomeServerSettingsViewState) {
        settingsCenteredImageItem {
            id("icon")
            drawableRes(R.drawable.ic_layers)
        }
        settingsSectionTitleItem {
            id("urlTitle")
            titleResId(R.string.hs_url)
        }
        settingsInfoItem {
            id("urlValue")
            helperText(state.homeserverUrl)
        }
        if (vectorPreferences.developerMode()) {
            settingsSectionTitleItem {
                id("urlApiTitle")
                titleResId(R.string.hs_client_url)
            }
            settingsInfoItem {
                id("urlApiValue")
                helperText(state.homeserverClientServerApiUrl)
            }
        }
    }

    private fun buildFederationVersion(federationVersion: FederationVersion) {
        settingsSectionTitleItem {
            id("nameTitle")
            titleResId(R.string.settings_server_name)
        }
        settingsInfoItem {
            id("nameValue")
            helperText(federationVersion.name)
        }
        settingsSectionTitleItem {
            id("versionTitle")
            titleResId(R.string.settings_server_version)
        }
        settingsInfoItem {
            id("versionValue")
            helperText(federationVersion.version)
        }
    }

    private fun buildCapabilities(data: HomeServerSettingsViewState) {
        val host = this
        settingsSectionTitleItem {
            id("uploadTitle")
            titleResId(R.string.settings_server_upload_size_title)
        }

        val limit = data.homeServerCapabilities.maxUploadFileSize

        settingsInfoItem {
            id("uploadValue")
            if (limit == HomeServerCapabilities.MAX_UPLOAD_FILE_SIZE_UNKNOWN) {
                helperTextResId(R.string.settings_server_upload_size_unknown)
            } else {
                helperText(host.stringProvider.getString(R.string.settings_server_upload_size_content, "${limit / 1048576L} MB"))
            }
        }

        if (vectorPreferences.developerMode()) {
            val roomCapabilities = data.homeServerCapabilities.roomVersions
            if (roomCapabilities != null) {
                settingsSectionTitleItem {
                    id("room_versions")
                    titleResId(R.string.settings_server_room_versions)
                }

                genericWithValueItem {
                    id("room_version_default")
                    title(host.stringProvider.getString(R.string.settings_server_default_room_version))
                    value(roomCapabilities.defaultRoomVersion)
                }

                roomCapabilities.supportedVersion.forEach {
                    genericWithValueItem {
                        id("room_version_${it.version}")
                        title(it.version)
                        value(
                                host.stringProvider.getString(
                                        when (it.status) {
                                            RoomVersionStatus.STABLE   -> R.string.settings_server_room_version_stable
                                            RoomVersionStatus.UNSTABLE -> R.string.settings_server_room_version_unstable
                                        }
                                )
                        )
                    }
                }
            }
        }
    }
}
