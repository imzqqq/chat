package com.imzqqq.app.features.home.room.detail.timeline

import androidx.annotation.ColorInt
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.features.home.room.detail.timeline.helper.MatrixItemColorProvider
import com.imzqqq.app.features.settings.VectorPreferences
import org.matrix.android.sdk.api.session.room.send.SendState
import org.matrix.android.sdk.api.util.MatrixItem
import javax.inject.Inject

class MessageColorProvider @Inject constructor(
        private val colorProvider: ColorProvider,
        private val matrixItemColorProvider: MatrixItemColorProvider,
        private val vectorPreferences: VectorPreferences) {

    @ColorInt
    fun getMemberNameTextColor(matrixItem: MatrixItem, userInRoomInformation: MatrixItemColorProvider.UserInRoomInformation? = null): Int {
        return matrixItemColorProvider.getColor(matrixItem, userInRoomInformation)
    }

    @ColorInt
    fun getMessageTextColor(sendState: SendState): Int {
        return if (vectorPreferences.developerMode()) {
            when (sendState) {
                // SendStates, in the classical order they will occur
                SendState.UNKNOWN,
                SendState.UNSENT                 -> colorProvider.getColorFromAttribute(R.attr.vctr_sending_message_text_color)
                SendState.ENCRYPTING             -> colorProvider.getColorFromAttribute(R.attr.vctr_encrypting_message_text_color)
                SendState.SENDING                -> colorProvider.getColorFromAttribute(R.attr.vctr_sending_message_text_color)
                SendState.SENT,
                SendState.SYNCED                 -> colorProvider.getColorFromAttribute(R.attr.vctr_message_text_color)
                SendState.UNDELIVERED,
                SendState.FAILED_UNKNOWN_DEVICES -> colorProvider.getColorFromAttribute(R.attr.vctr_unsent_message_text_color)
            }
        } else {
            // When not in developer mode, we use only one color
            colorProvider.getColorFromAttribute(R.attr.vctr_message_text_color)
        }
    }
}
