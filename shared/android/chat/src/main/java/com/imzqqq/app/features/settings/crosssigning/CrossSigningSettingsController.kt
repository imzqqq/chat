package com.imzqqq.app.features.settings.crosssigning

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.genericButtonItem
import com.imzqqq.app.core.ui.list.genericItem
import com.imzqqq.app.core.ui.list.genericPositiveButtonItem
import com.imzqqq.app.core.ui.list.genericWithValueItem
import com.imzqqq.app.core.utils.DimensionConverter
import me.gujun.android.span.span
import javax.inject.Inject

class CrossSigningSettingsController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider,
        private val dimensionConverter: DimensionConverter
) : TypedEpoxyController<CrossSigningSettingsViewState>() {

    interface InteractionListener {
        fun didTapInitializeCrossSigning()
    }

    var interactionListener: InteractionListener? = null

    override fun buildModels(data: CrossSigningSettingsViewState?) {
        if (data == null) return
        val host = this
        when {
            data.xSigningKeyCanSign        -> {
                genericItem {
                    id("can")
                    titleIconResourceId(R.drawable.ic_shield_trusted)
                    title(host.stringProvider.getString(R.string.encryption_information_dg_xsigning_complete))
                }
                genericButtonItem {
                    id("Reset")
                    text(host.stringProvider.getString(R.string.reset_cross_signing))
                    buttonClickAction {
                        host.interactionListener?.didTapInitializeCrossSigning()
                    }
                }
            }
            data.xSigningKeysAreTrusted    -> {
                genericItem {
                    id("trusted")
                    titleIconResourceId(R.drawable.ic_shield_custom)
                    title(host.stringProvider.getString(R.string.encryption_information_dg_xsigning_trusted))
                }
                genericButtonItem {
                    id("Reset")
                    text(host.stringProvider.getString(R.string.reset_cross_signing))
                    buttonClickAction {
                        host.interactionListener?.didTapInitializeCrossSigning()
                    }
                }
            }
            data.xSigningIsEnableInAccount -> {
                genericItem {
                    id("enable")
                    titleIconResourceId(R.drawable.ic_shield_black)
                    title(host.stringProvider.getString(R.string.encryption_information_dg_xsigning_not_trusted))
                }
                genericButtonItem {
                    id("Reset")
                    text(host.stringProvider.getString(R.string.reset_cross_signing))
                    buttonClickAction {
                        host.interactionListener?.didTapInitializeCrossSigning()
                    }
                }
            }
            else                           -> {
                genericItem {
                    id("not")
                    title(host.stringProvider.getString(R.string.encryption_information_dg_xsigning_disabled))
                }

                genericPositiveButtonItem {
                    id("Initialize")
                    text(host.stringProvider.getString(R.string.initialize_cross_signing))
                    buttonClickAction {
                        host.interactionListener?.didTapInitializeCrossSigning()
                    }
                }
            }
        }

        val crossSigningKeys = data.crossSigningInfo

        crossSigningKeys?.masterKey()?.let {
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
        crossSigningKeys?.userKey()?.let {
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
        crossSigningKeys?.selfSigningKey()?.let {
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
