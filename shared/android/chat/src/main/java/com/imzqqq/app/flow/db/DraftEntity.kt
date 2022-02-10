package com.imzqqq.app.flow.db

import android.net.Uri
import android.os.Parcelable
import androidx.core.net.toUri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.imzqqq.app.flow.entity.NewPoll
import com.imzqqq.app.flow.entity.Status
import kotlinx.parcelize.Parcelize

@Entity
@TypeConverters(Converters::class)
data class DraftEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val accountId: Long,
        val inReplyToId: String?,
        val content: String?,
        val contentWarning: String?,
        val sensitive: Boolean,
        val visibility: Status.Visibility,
        val attachments: List<DraftAttachment>,
        val poll: NewPoll?,
        val failedToSend: Boolean
)

@Parcelize
data class DraftAttachment(
    val uriString: String,
    val description: String?,
    val type: Type
) : Parcelable {
    val uri: Uri
        get() = uriString.toUri()

    enum class Type {
        IMAGE, VIDEO, AUDIO;
    }
}
