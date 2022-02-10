package com.imzqqq.app.features.crypto.keys

import android.content.Context
import android.net.Uri
import com.imzqqq.app.core.intent.getMimeTypeFromUri
import com.imzqqq.app.core.resources.openResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.internal.crypto.model.ImportRoomKeysResult
import javax.inject.Inject

class KeysImporter @Inject constructor(
        private val context: Context,
        private val session: Session
) {
    /**
     * Import keys from provided Uri
     */
    suspend fun import(uri: Uri,
                       mimetype: String?,
                       password: String): ImportRoomKeysResult {
        return withContext(Dispatchers.IO) {
            val resource = openResource(context, uri, mimetype ?: getMimeTypeFromUri(context, uri))
            val stream = resource?.mContentStream ?: throw Exception("Error")
            val data = stream.use { it.readBytes() }
            session.cryptoService().importRoomKeys(data, password, null)
        }
    }
}
