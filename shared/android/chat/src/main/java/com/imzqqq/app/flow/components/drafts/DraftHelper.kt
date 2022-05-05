package com.imzqqq.app.flow.components.drafts

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.imzqqq.app.BuildConfig
import com.imzqqq.app.flow.db.AppDatabase
import com.imzqqq.app.flow.db.DraftAttachment
import com.imzqqq.app.flow.db.DraftEntity
import com.imzqqq.app.flow.entity.NewPoll
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.util.IOUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DraftHelper @Inject constructor(
    val context: Context,
    db: AppDatabase
) {

    private val draftDao = db.draftDao()

    suspend fun saveDraft(
            draftId: Int,
            accountId: Long,
            inReplyToId: String?,
            content: String?,
            contentWarning: String?,
            sensitive: Boolean,
            visibility: Status.Visibility,
            mediaUris: List<String>,
            mediaDescriptions: List<String?>,
            poll: NewPoll?,
            failedToSend: Boolean
    ) = withContext(Dispatchers.IO) {
        val externalFilesDir = context.getExternalFilesDir("downloads")

        if (externalFilesDir == null || !(externalFilesDir.exists())) {
            Timber.e("Error obtaining directory to save media.")
            throw Exception()
        }

        val draftDirectory = File(externalFilesDir, "Drafts")

        if (!draftDirectory.exists()) {
            draftDirectory.mkdir()
        }

        val uris = mediaUris.map { uriString ->
            uriString.toUri()
        }.map { uri ->
            if (uri.isNotInFolder(draftDirectory)) {
                uri.copyToFolder(draftDirectory)
            } else {
                uri
            }
        }

        val types = uris.map { uri ->
            val mimeType = context.contentResolver.getType(uri)
            when (mimeType?.substring(0, mimeType.indexOf('/'))) {
                "video" -> DraftAttachment.Type.VIDEO
                "image" -> DraftAttachment.Type.IMAGE
                "audio" -> DraftAttachment.Type.AUDIO
                else -> throw IllegalStateException("unknown media type")
            }
        }

        val attachments: MutableList<DraftAttachment> = mutableListOf()
        for (i in mediaUris.indices) {
            attachments.add(
                DraftAttachment(
                    uriString = uris[i].toString(),
                    description = mediaDescriptions[i],
                    type = types[i]
                )
            )
        }

        val draft = DraftEntity(
            id = draftId,
            accountId = accountId,
            inReplyToId = inReplyToId,
            content = content,
            contentWarning = contentWarning,
            sensitive = sensitive,
            visibility = visibility,
            attachments = attachments,
            poll = poll,
            failedToSend = failedToSend
        )

        draftDao.insertOrReplace(draft)
    }

    suspend fun deleteDraftAndAttachments(draftId: Int) {
        draftDao.find(draftId)?.let { draft ->
            deleteDraftAndAttachments(draft)
        }
    }

    suspend fun deleteDraftAndAttachments(draft: DraftEntity) {
        deleteAttachments(draft)
        draftDao.delete(draft.id)
    }

    suspend fun deleteAllDraftsAndAttachmentsForAccount(accountId: Long) {
        draftDao.loadDrafts(accountId).forEach { draft ->
            deleteDraftAndAttachments(draft)
        }
    }

    suspend fun deleteAttachments(draft: DraftEntity) {
        withContext(Dispatchers.IO) {
            draft.attachments.forEach { attachment ->
                if (context.contentResolver.delete(attachment.uri, null, null) == 0) {
                    Timber.e("Did not delete file " + attachment.uriString)
                }
            }
        }
    }

    private fun Uri.isNotInFolder(folder: File): Boolean {
        val filePath = path ?: return true
        return File(filePath).parentFile == folder
    }

    private fun Uri.copyToFolder(folder: File): Uri {
        val contentResolver = context.contentResolver

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

        val mimeType = contentResolver.getType(this)
        val map = MimeTypeMap.getSingleton()
        val fileExtension = map.getExtensionFromMimeType(mimeType)

        val filename = String.format("Flow_Draft_Media_%s.%s", timeStamp, fileExtension)
        val file = File(folder, filename)
        com.imzqqq.app.flow.util.IOUtils.copyToFile(contentResolver, this, file)
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file)
    }
}
