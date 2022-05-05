package com.keylesspalace.flow.viewdata

import android.os.Parcelable
import com.keylesspalace.flow.entity.Attachment
import com.keylesspalace.flow.entity.Status
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttachmentViewData(
    val attachment: Attachment,
    val statusId: String,
    val statusUrl: String
) : Parcelable {
    companion object {
        @JvmStatic
        fun list(status: Status): List<AttachmentViewData> {
            val actionable = status.actionableStatus
            return actionable.attachments.map {
                AttachmentViewData(it, actionable.id, actionable.url!!)
            }
        }
    }
}
