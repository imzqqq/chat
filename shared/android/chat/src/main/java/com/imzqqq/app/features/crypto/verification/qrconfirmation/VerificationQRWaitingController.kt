package com.imzqqq.app.features.crypto.verification.qrconfirmation

import com.airbnb.epoxy.EpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationBigImageItem
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationNoticeItem
import com.imzqqq.app.features.crypto.verification.epoxy.bottomSheetVerificationWaitingItem
import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel
import javax.inject.Inject

class VerificationQRWaitingController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider
) : EpoxyController() {

    private var args: VerificationQRWaitingFragment.Args? = null

    fun update(args: VerificationQRWaitingFragment.Args) {
        this.args = args
        requestModelBuild()
    }

    override fun buildModels() {
        val params = args ?: return
        val host = this

        bottomSheetVerificationNoticeItem {
            id("notice")
            apply {
                notice(host.stringProvider.getString(R.string.qr_code_scanned_verif_waiting_notice))
            }
        }

        bottomSheetVerificationBigImageItem {
            id("image")
            roomEncryptionTrustLevel(RoomEncryptionTrustLevel.Trusted)
        }

        bottomSheetVerificationWaitingItem {
            id("waiting")
            title(host.stringProvider.getString(R.string.qr_code_scanned_verif_waiting, params.otherUserName))
        }
    }
}
