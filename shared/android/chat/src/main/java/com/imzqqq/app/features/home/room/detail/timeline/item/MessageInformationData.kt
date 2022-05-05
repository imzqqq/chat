package com.imzqqq.app.features.home.room.detail.timeline.item

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.crypto.VerificationState
import org.matrix.android.sdk.api.session.room.send.SendState
import org.matrix.android.sdk.api.util.MatrixItem

@Parcelize
data class MessageInformationData(
        val eventId: String,
        val senderId: String,
        val sendState: SendState,
        val time: CharSequence? = null,
        val ageLocalTS: Long?,
        val avatarUrl: String?,
        val memberName: CharSequence? = null,
        val showInformation: Boolean = true,
        val forceShowTimestamp: Boolean = false,
        /*List of reactions (emoji,count,isSelected)*/
        val orderedReactionList: List<ReactionInfoData>? = null,
        val pollResponseAggregatedSummary: PollResponseData? = null,
        val hasBeenEdited: Boolean = false,
        val hasPendingEdits: Boolean = false,
        val referencesInfoData: ReferencesInfoData? = null,
        val sentByMe : Boolean,
        val readReceiptAnonymous: AnonymousReadReceipt,
        val isDirect: Boolean,
        val isPublic: Boolean,
        val senderPowerLevel: Int?,
        val dmChatPartnerId: String?,
        val e2eDecoration: E2EDecoration = E2EDecoration.NONE,
        val sendStateDecoration: SendStateDecoration = SendStateDecoration.NONE
) : Parcelable {

    val matrixItem: MatrixItem
        get() = MatrixItem.UserItem(senderId, memberName?.toString(), avatarUrl)
}

@Parcelize
data class ReferencesInfoData(
        val verificationStatus: VerificationState
) : Parcelable

@Parcelize
data class ReactionInfoData(
        val key: String,
        val count: Int,
        val addedByMe: Boolean,
        val synced: Boolean
) : Parcelable

@Parcelize
data class ReadReceiptData(
        val userId: String,
        val avatarUrl: String?,
        val displayName: String?,
        val timestamp: Long
) : Parcelable

@Parcelize
data class PollResponseData(
        val myVote: Int?,
        val votes: Map<Int, Int>?,
        val isClosed: Boolean = false
) : Parcelable

enum class E2EDecoration {
    NONE,
    WARN_IN_CLEAR,
    WARN_SENT_BY_UNVERIFIED,
    WARN_SENT_BY_UNKNOWN
}

enum class AnonymousReadReceipt {
    NONE,
    PROCESSING,
    // For future use?
    //SENT,
    //READ
}

enum class SendStateDecoration {
    NONE,
    SENDING_NON_MEDIA,
    SENDING_MEDIA,
    SENT,
    FAILED
}

fun ReadReceiptData.toMatrixItem() = MatrixItem.UserItem(userId, displayName, avatarUrl)
