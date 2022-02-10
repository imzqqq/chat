package com.imzqqq.app.features.crypto.verification.qrconfirmation

import com.airbnb.epoxy.EpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.bottomSheetDividerItem
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.crypto.verification.VerificationBottomSheetViewState
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationActionItem
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationBigImageItem
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationNoticeItem
import com.imzqqq.app.features.displayname.getBestName
import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel
import javax.inject.Inject

class VerificationQrScannedByOtherController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider
) : EpoxyController() {

    var listener: Listener? = null

    private var viewState: VerificationBottomSheetViewState? = null

    fun update(viewState: VerificationBottomSheetViewState) {
        this.viewState = viewState
        requestModelBuild()
    }

    override fun buildModels() {
        val state = viewState ?: return
        val host = this

        bottomSheetVerificationNoticeItem {
            id("notice")
            apply {
                if (state.isMe) {
                    notice(host.stringProvider.getString(R.string.qr_code_scanned_self_verif_notice))
                } else {
                    val name = state.otherUserMxItem?.getBestName() ?: ""
                    notice(host.stringProvider.getString(R.string.qr_code_scanned_by_other_notice, name))
                }
            }
        }

        bottomSheetVerificationBigImageItem {
            id("image")
            roomEncryptionTrustLevel(RoomEncryptionTrustLevel.Trusted)
        }

        bottomSheetDividerItem {
            id("sep0")
        }

        bottomSheetVerificationActionItem {
            id("deny")
            title(host.stringProvider.getString(R.string.qr_code_scanned_by_other_no))
            titleColor(host.colorProvider.getColorFromAttribute(R.attr.colorError))
            iconRes(R.drawable.ic_check_off)
            iconColor(host.colorProvider.getColorFromAttribute(R.attr.colorError))
            listener { host.listener?.onUserDeniesQrCodeScanned() }
        }

        bottomSheetDividerItem {
            id("sep1")
        }

        bottomSheetVerificationActionItem {
            id("confirm")
            title(host.stringProvider.getString(R.string.qr_code_scanned_by_other_yes))
            titleColor(host.colorProvider.getColorFromAttribute(R.attr.colorPrimary))
            iconRes(R.drawable.ic_check_on)
            iconColor(host.colorProvider.getColorFromAttribute(R.attr.colorPrimary))
            listener { host.listener?.onUserConfirmsQrCodeScanned() }
        }
    }

    interface Listener {
        fun onUserConfirmsQrCodeScanned()
        fun onUserDeniesQrCodeScanned()
    }
}
