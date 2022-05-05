package com.imzqqq.app.features.roommemberprofile.devices

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.errorWithRetryItem
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.ItemStyle
import com.imzqqq.app.core.ui.list.genericFooterItem
import com.imzqqq.app.core.ui.list.genericItem
import com.imzqqq.app.core.ui.list.genericWithValueItem
import com.imzqqq.app.core.utils.DimensionConverter
import com.imzqqq.app.features.settings.VectorPreferences
import me.gujun.android.span.span
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo
import javax.inject.Inject

class DeviceListEpoxyController @Inject constructor(private val stringProvider: StringProvider,
                                                    private val colorProvider: ColorProvider,
                                                    private val dimensionConverter: DimensionConverter,
                                                    private val vectorPreferences: VectorPreferences) :
    TypedEpoxyController<DeviceListViewState>() {

    interface InteractionListener {
        fun onDeviceSelected(device: CryptoDeviceInfo)
    }

    var interactionListener: InteractionListener? = null

    override fun buildModels(data: DeviceListViewState?) {
        data ?: return
        val host = this
        when (data.cryptoDevices) {
            Uninitialized -> {
            }
            is Loading    -> {
                loadingItem {
                    id("loading")
                    loadingText(host.stringProvider.getString(R.string.loading))
                }
            }
            is Success    -> {
                val deviceList = data.cryptoDevices.invoke().sortedBy {
                    it.isVerified
                }

                // Build top header
                val allGreen = deviceList.fold(true, { prev, device ->
                    prev && device.isVerified
                })

                genericItem {
                    id("title")
                    style(ItemStyle.BIG_TEXT)
                    titleIconResourceId(if (allGreen) R.drawable.ic_shield_trusted else R.drawable.ic_shield_warning)
                    title(
                            host.stringProvider.getString(
                                    if (allGreen) R.string.verification_profile_verified else R.string.verification_profile_warning
                            )
                    )
                    description(host.stringProvider.getString(R.string.verification_conclusion_ok_notice))
                }

                if (vectorPreferences.developerMode()) {
                    // Display the cross signing keys
                    addDebugInfo(data)
                }

                genericItem {
                    id("sessions")
                    style(ItemStyle.BIG_TEXT)
                    title(host.stringProvider.getString(R.string.room_member_profile_sessions_section_title))
                }
                if (deviceList.isEmpty()) {
                    // Can this really happen?
                    genericFooterItem {
                        id("empty")
                        text(host.stringProvider.getString(R.string.search_no_results))
                    }
                } else {
                    // Build list of device with status
                    deviceList.forEach { device ->
                        genericWithValueItem {
                            id(device.deviceId)
                            titleIconResourceId(if (device.isVerified) R.drawable.ic_shield_trusted else R.drawable.ic_shield_warning)
                            apply {
                                if (host.vectorPreferences.developerMode()) {
                                    val seq = span {
                                        +(device.displayName() ?: device.deviceId)
                                        +"\n"
                                        span {
                                            text = "(${device.deviceId})"
                                            textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                                            textSize = host.dimensionConverter.spToPx(14)
                                        }
                                    }
                                    title(seq)
                                } else {
                                    title(device.displayName() ?: device.deviceId)
                                }
                            }
                            value(
                                    host.stringProvider.getString(
                                            if (device.isVerified) R.string.trusted else R.string.not_trusted
                                    )
                            )
                            valueColorInt(
                                    host.colorProvider.getColorFromAttribute(
                                            if (device.isVerified) R.attr.colorPrimary else R.attr.colorError
                                    )
                            )
                            itemClickAction {
                                host.interactionListener?.onDeviceSelected(device)
                            }
                        }
                    }
                }
            }
            is Fail       -> {
                errorWithRetryItem {
                    id("error")
                    text(host.stringProvider.getString(R.string.room_member_profile_failed_to_get_devices))
                    listener {
                        // TODO
                    }
                }
            }
        }
    }

    private fun addDebugInfo(data: DeviceListViewState) {
        val host = this
        data.memberCrossSigningKey?.masterKey()?.let {
            genericWithValueItem {
                id("msk")
                titleIconResourceId(R.drawable.key_small)
                title(
                        span {
                            +"Master Key:\n"
                            span {
                                text = it.unpaddedBase64PublicKey ?: ""
                                textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                                textSize = host.dimensionConverter.spToPx(12)
                            }
                        }
                )
            }
        }
        data.memberCrossSigningKey?.userKey()?.let {
            genericWithValueItem {
                id("usk")
                titleIconResourceId(R.drawable.key_small)
                title(
                        span {
                            +"User Key:\n"
                            span {
                                text = it.unpaddedBase64PublicKey ?: ""
                                textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                                textSize = host.dimensionConverter.spToPx(12)
                            }
                        }
                )
            }
        }
        data.memberCrossSigningKey?.selfSigningKey()?.let {
            genericWithValueItem {
                id("ssk")
                titleIconResourceId(R.drawable.key_small)
                title(
                        span {
                            +"Self Signed Key:\n"
                            span {
                                text = it.unpaddedBase64PublicKey ?: ""
                                textColor = host.colorProvider.getColorFromAttribute(R.attr.vctr_content_secondary)
                                textSize = host.dimensionConverter.spToPx(12)
                            }
                        }
                )
            }
        }
    }
}
