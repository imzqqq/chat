/* Copyright 2021 Flow Contributors
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.components.drafts

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.keylesspalace.flow.BuildConfig
import com.keylesspalace.flow.db.AppDatabase
import com.keylesspalace.flow.db.DraftAttachment
import com.keylesspalace.flow.db.DraftEntity
import com.keylesspalace.flow.entity.NewPoll
import com.keylesspalace.flow.entity.Status
import com.keylesspalace.flow.util.IOUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        val externalFilesDir = context.getExternalFilesDir("Flow")

        if (externalFilesDir == null || !(externalFilesDir.exists())) {
            Log.e("DraftHelper", "Error obtaining directory to save media.")
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
                    Log.e("DraftHelper", "Did not delete file ${attachment.uriString}")
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
        IOUtils.copyToFile(contentResolver, this, file)
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
    }
}
